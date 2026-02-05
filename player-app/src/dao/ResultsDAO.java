package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBUtil;

public class ResultsDAO {

    /**
     * 決勝が確定していたら results に保存する（大会ごとに1レコード）。
     * 戻り値:
     *  true  = 保存した（or 上書きした）
     *  false = 決勝未確定のため何もしなかった
     */
    public boolean upsertIfFinalized(int tournamentId) throws SQLException {

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 開催日を tournaments から取る（results.event_date はNN）
                java.sql.Date date = fetchEventDate(con, tournamentId);
                if (date == null) {
                    // event_date が取れないなら今日を入れる（NN対策）
                    date = new java.sql.Date(System.currentTimeMillis());
                }

                int maxRound = fetchMaxRound(con, tournamentId);
                if (maxRound <= 0) {
                    con.rollback();
                    return false;
                }

                // 決勝（最終ラウンド）は match_index=0 の1試合想定
                MatchRow fin = fetchMatch(con, tournamentId, maxRound, 0);
                if (fin == null || fin.winnerId == null) {
                    con.rollback();
                    return false; // 決勝未確定
                }

                int firstId = fin.winnerId;
                int secondId = (firstId == fin.player1Id) ? fin.player2Id : fin.player1Id;

                // 名前は participants（スナップショット）から取る
                Map<Integer, String> nameMap = fetchNames(con, tournamentId, Arrays.asList(firstId, secondId));

                String firstName = safeName(nameMap.get(firstId));
                String secondName = safeName(nameMap.get(secondId));

                // 3位（準決敗者2名）を "A / B" で1フィールドにまとめる
                String thirdName = "該当なし";
                if (maxRound >= 2) {
                    List<MatchRow> semis = fetchMatchesByRound(con, tournamentId, maxRound - 1);

                    if (!semis.isEmpty()) {
                        List<Integer> thirdIds = new ArrayList<>();
                        for (MatchRow s : semis) {
                            if (s.winnerId == null) {
                                con.rollback();
                                return false;
                            }
                            int loser = (s.winnerId == s.player1Id) ? s.player2Id : s.player1Id;
                            thirdIds.add(loser);
                        }

                        Map<Integer, String> thirdNameMap = fetchNames(con, tournamentId, thirdIds);
                        List<String> thirdNames = new ArrayList<>();
                        for (int pid : thirdIds) {
                            thirdNames.add(safeName(thirdNameMap.get(pid)));
                        }

                        thirdName = String.join(" / ", thirdNames);
                        if (thirdName.trim().isEmpty()) thirdName = "該当なし";
                    }
                }

                upsert(con, tournamentId, firstName, secondName, thirdName, date);

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // ===== private =====

    private java.sql.Date fetchEventDate(Connection con, int tournamentId) throws SQLException {
        String sql = "SELECT event_date FROM tournaments WHERE tournament_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getDate("event_date");
            }
        }
    }

    private int fetchMaxRound(Connection con, int tournamentId) throws SQLException {
        String sql = "SELECT MAX(round_no) AS mx FROM matches WHERE tournament_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getInt("mx");
            }
        }
    }

    private MatchRow fetchMatch(Connection con, int tournamentId, int roundNo, int matchIndex) throws SQLException {
        String sql =
            "SELECT player1_id, player2_id, winner_id " +
            "FROM matches " +
            "WHERE tournament_id = ? AND round_no = ? AND match_index = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ps.setInt(2, roundNo);
            ps.setInt(3, matchIndex);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Integer p1 = getNullableInt(rs, "player1_id");
                Integer p2 = getNullableInt(rs, "player2_id");
                Integer w  = getNullableInt(rs, "winner_id");
                if (p1 == null || p2 == null) return null;

                MatchRow m = new MatchRow();
                m.player1Id = p1;
                m.player2Id = p2;
                m.winnerId = w;
                return m;
            }
        }
    }

    private List<MatchRow> fetchMatchesByRound(Connection con, int tournamentId, int roundNo) throws SQLException {
        String sql =
            "SELECT player1_id, player2_id, winner_id " +
            "FROM matches " +
            "WHERE tournament_id = ? AND round_no = ? " +
            "ORDER BY match_index";

        List<MatchRow> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ps.setInt(2, roundNo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer p1 = getNullableInt(rs, "player1_id");
                    Integer p2 = getNullableInt(rs, "player2_id");
                    Integer w  = getNullableInt(rs, "winner_id");
                    if (p1 == null || p2 == null) continue;

                    MatchRow m = new MatchRow();
                    m.player1Id = p1;
                    m.player2Id = p2;
                    m.winnerId = w;
                    list.add(m);
                }
            }
        }
        return list;
    }

    private Map<Integer, String> fetchNames(Connection con, int tournamentId, List<Integer> ids) throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) return map;

        StringBuilder in = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) in.append(",");
            in.append("?");
        }

        String sql =
            "SELECT player_id, player_name " +
            "FROM participants " +
            "WHERE tournament_id = ? AND player_id IN (" + in + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int idx = 1;
            ps.setInt(idx++, tournamentId);
            for (int pid : ids) ps.setInt(idx++, pid);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("player_id"), rs.getString("player_name"));
                }
            }
        }

        return map;
    }

    private void upsert(Connection con, int tournamentId,
                        String first, String second, String third,
                        java.sql.Date eventDate) throws SQLException {

        String sql =
            "INSERT INTO results (tournament_id, firstplace_player, secondplace_player, thirdplace_player, event_date) " +
            "VALUES (?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "  firstplace_player = VALUES(firstplace_player), " +
            "  secondplace_player = VALUES(secondplace_player), " +
            "  thirdplace_player = VALUES(thirdplace_player), " +
            "  event_date = VALUES(event_date)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ps.setString(2, first);
            ps.setString(3, second);
            ps.setString(4, third);
            ps.setDate(5, eventDate);
            ps.executeUpdate();
        }
    }

    private String safeName(String s) {
        if (s == null) return "unknown";
        String t = s.trim();
        return t.isEmpty() ? "unknown" : t;
    }

    private Integer getNullableInt(ResultSet rs, String col) throws SQLException {
        int v = rs.getInt(col);
        return rs.wasNull() ? null : v;
    }

    private static class MatchRow {
        int player1Id;
        int player2Id;
        Integer winnerId;
    }
}
