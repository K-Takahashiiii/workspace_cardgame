package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bean.Match;
import util.DBUtil;

public class MatchesDAO {

    /** 既にmatchesがあれば何もしない（true=新規作成, false=既に存在） */
    public boolean createBracketIfAbsent(int tournamentId) throws SQLException {

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            // 既に作成済み？
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT 1 FROM matches WHERE tournament_id = ? LIMIT 1")) {
                ps.setInt(1, tournamentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 参加者取得（シャッフルはIDだけでOK）
            List<Integer> playerIds = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT player_id FROM participants WHERE tournament_id = ?")) {
                ps.setInt(1, tournamentId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        playerIds.add(rs.getInt("player_id"));
                    }
                }
            }

            int n = playerIds.size();
            if (n < 2) throw new SQLException("参加者が2人未満です（tournament_id=" + tournamentId + "）");

            // 2の冪チェック（2,4,8,16...）
            if ((n & (n - 1)) != 0) {
                throw new SQLException("参加人数が2の冪ではありません（n=" + n + "）");
            }

            // 何回戦か（log不要：半分にして数える）
            int rounds = 0;
            for (int tmp = n; tmp > 1; tmp /= 2) rounds++;

            // ランダム
            Collections.shuffle(playerIds);

            String ins =
                "INSERT INTO matches (tournament_id, round_no, match_index, player1_id, player2_id) " +
                "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement psIns = con.prepareStatement(ins)) {

                // Round1：対戦者あり
                int matchCount = n / 2;
                for (int i = 0; i < matchCount; i++) {
                    int p1 = playerIds.get(i * 2);
                    int p2 = playerIds.get(i * 2 + 1);

                    psIns.setInt(1, tournamentId);
                    psIns.setInt(2, 1);
                    psIns.setInt(3, i); // 0始まり
                    psIns.setInt(4, p1);
                    psIns.setInt(5, p2);
                    psIns.addBatch();
                }

                // Round2以降：枠だけ（NULL）
                int mc = matchCount;
                for (int r = 2; r <= rounds; r++) {
                    mc /= 2;
                    for (int i = 0; i < mc; i++) {
                        psIns.setInt(1, tournamentId);
                        psIns.setInt(2, r);
                        psIns.setInt(3, i);
                        psIns.setNull(4, Types.INTEGER);
                        psIns.setNull(5, Types.INTEGER);
                        psIns.addBatch();
                    }
                }

                psIns.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException ignore) {}
                try { con.close(); } catch (SQLException ignore) {}
            }
        }
    }

    /** ブラケット表示用：名前は participants からJOINして毎回付ける（試合数が少ないから軽い） */
    public List<Match> findByTournamentId(int tournamentId) throws SQLException {

        String sql =
            "SELECT m.match_id, m.tournament_id, m.round_no, m.match_index, " +
            "       m.player1_id, m.player2_id, m.winner_id, m.decided_at, " +
            "       p1.player_name AS p1_name, " +
            "       p2.player_name AS p2_name, " +
            "       pw.player_name AS w_name " +
            "FROM matches m " +
            "LEFT JOIN participants p1 ON p1.tournament_id = m.tournament_id AND p1.player_id = m.player1_id " +
            "LEFT JOIN participants p2 ON p2.tournament_id = m.tournament_id AND p2.player_id = m.player2_id " +
            "LEFT JOIN participants pw ON pw.tournament_id = m.tournament_id AND pw.player_id = m.winner_id " +
            "WHERE m.tournament_id = ? " +
            "ORDER BY m.round_no, m.match_index";

        List<Match> list = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tournamentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readMatchRow(rs));
                }
            }
        }

        return list;
    }

    /** 勝者入力画面用：1試合だけ取得 */
    public Match findOne(int tournamentId, int matchId) throws SQLException {

        String sql =
            "SELECT m.match_id, m.tournament_id, m.round_no, m.match_index, " +
            "       m.player1_id, m.player2_id, m.winner_id, m.decided_at, " +
            "       p1.player_name AS p1_name, " +
            "       p2.player_name AS p2_name, " +
            "       pw.player_name AS w_name " +
            "FROM matches m " +
            "LEFT JOIN participants p1 ON p1.tournament_id = m.tournament_id AND p1.player_id = m.player1_id " +
            "LEFT JOIN participants p2 ON p2.tournament_id = m.tournament_id AND p2.player_id = m.player2_id " +
            "LEFT JOIN participants pw ON pw.tournament_id = m.tournament_id AND pw.player_id = m.winner_id " +
            "WHERE m.tournament_id = ? AND m.match_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tournamentId);
            ps.setInt(2, matchId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return readMatchRow(rs);
            }
        }
    }

    /**
     * 勝者確定 → 次ラウンドへ繰り上げ（同一トランザクション）
     * 戻り値:
     *   1 = 成功
     *   0 = 既に確定済み（or 同時更新で負けた）
     *  -1 = 不正（未成立/当事者でない/勝者が対戦者でない 等）
     */
    public int reportWinner(int tournamentId, int matchId, int winnerId, int reporterUserNum) throws SQLException {

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Integer p1 = null, p2 = null, w = null;
            int roundNo, matchIndex;

            // 対象試合をロックして取得
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT round_no, match_index, player1_id, player2_id, winner_id " +
                    "FROM matches WHERE match_id = ? AND tournament_id = ? FOR UPDATE")) {
                ps.setInt(1, matchId);
                ps.setInt(2, tournamentId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return -1;
                    }
                    roundNo = rs.getInt("round_no");
                    matchIndex = rs.getInt("match_index");

                    int tp1 = rs.getInt("player1_id");
                    p1 = rs.wasNull() ? null : tp1;

                    int tp2 = rs.getInt("player2_id");
                    p2 = rs.wasNull() ? null : tp2;

                    int tw = rs.getInt("winner_id");
                    w = rs.wasNull() ? null : tw;
                }
            }

            // 既に確定済み
            if (w != null) {
                con.rollback();
                return 0;
            }

            // 試合未成立（どっちかが未確定）
            if (p1 == null || p2 == null) {
                con.rollback();
                return -1;
            }

            // 報告者は当事者のみ
            if (reporterUserNum != p1.intValue() && reporterUserNum != p2.intValue()) {
                con.rollback();
                return -1;
            }

            // 勝者は p1/p2 のどっちか
            if (winnerId != p1.intValue() && winnerId != p2.intValue()) {
                con.rollback();
                return -1;
            }

            // 勝者確定（未確定のみ更新）
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE matches SET winner_id = ?, decided_at = NOW() " +
                    "WHERE match_id = ? AND tournament_id = ? AND winner_id IS NULL")) {
                ps.setInt(1, winnerId);
                ps.setInt(2, matchId);
                ps.setInt(3, tournamentId);

                int updated = ps.executeUpdate();
                if (updated != 1) {
                    con.rollback();
                    return 0;
                }
            }

            // 最終ラウンド判定
            int maxRound = 0;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT MAX(round_no) AS mx FROM matches WHERE tournament_id = ?")) {
                ps.setInt(1, tournamentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) maxRound = rs.getInt("mx");
                }
            }

            // 次ラウンドへ繰り上げ（最終ラウンドは不要）
            if (roundNo < maxRound) {
                int nextRound = roundNo + 1;
                int nextIndex = matchIndex / 2;
                boolean toP1Slot = (matchIndex % 2 == 0);

                String up =
                    toP1Slot
                    ? "UPDATE matches SET player1_id = ? " +
                      "WHERE tournament_id = ? AND round_no = ? AND match_index = ? " +
                      "  AND (player1_id IS NULL OR player1_id = ?)"
                    : "UPDATE matches SET player2_id = ? " +
                      "WHERE tournament_id = ? AND round_no = ? AND match_index = ? " +
                      "  AND (player2_id IS NULL OR player2_id = ?)";

                try (PreparedStatement ps = con.prepareStatement(up)) {
                    ps.setInt(1, winnerId);
                    ps.setInt(2, tournamentId);
                    ps.setInt(3, nextRound);
                    ps.setInt(4, nextIndex);
                    ps.setInt(5, winnerId);
                    ps.executeUpdate();
                }
            }

            con.commit();
            return 1;

        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException ignore) {}
                try { con.close(); } catch (SQLException ignore) {}
            }
        }
    }

    // ===== private =====

    private Match readMatchRow(ResultSet rs) throws SQLException {
        Match m = new Match();
        m.setMatchId(rs.getInt("match_id"));
        m.setTournamentId(rs.getInt("tournament_id"));
        m.setRoundNo(rs.getInt("round_no"));
        m.setMatchIndex(rs.getInt("match_index"));

        int p1 = rs.getInt("player1_id");
        m.setPlayer1Id(rs.wasNull() ? null : p1);

        int p2 = rs.getInt("player2_id");
        m.setPlayer2Id(rs.wasNull() ? null : p2);

        int w = rs.getInt("winner_id");
        m.setWinnerId(rs.wasNull() ? null : w);

        m.setDecidedAt(rs.getTimestamp("decided_at"));

        m.setPlayer1Name(rs.getString("p1_name"));
        m.setPlayer2Name(rs.getString("p2_name"));
        m.setWinnerName(rs.getString("w_name"));

        return m;
    }
}
