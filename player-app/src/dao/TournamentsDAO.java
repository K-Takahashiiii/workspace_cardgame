package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bean.Tournament;
import util.DBUtil;

public class TournamentsDAO {

    // プレイヤー側：大会一覧（必要最低限 + 参加者数はCOUNTで取得）
    public List<Tournament> findAll() throws SQLException {

        String sql =
            "SELECT " +
            "  t.tournament_id, t.name, t.venue, t.entry_requirement, " +
            "  t.event_date, t.event_time, t.registration_deadline, " +
            "  t.max_participants, t.status, " +
            "  COALESCE(p.cnt, 0) AS participant_count " +
            "FROM tournaments t " +
            "LEFT JOIN ( " +
            "  SELECT tournament_id, COUNT(*) AS cnt " +
            "  FROM participants " +
            "  GROUP BY tournament_id " +
            ") p ON p.tournament_id = t.tournament_id " +
            "ORDER BY t.tournament_id DESC";

        List<Tournament> list = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tournament t = new Tournament();

                t.setTournamentId(rs.getInt("tournament_id"));
                t.setName(rs.getString("name"));
                t.setVenue(rs.getString("venue"));
                t.setEntryRequirement(rs.getInt("entry_requirement"));

                Date d = rs.getDate("event_date");
                if (d != null) t.setEventDate(d.toLocalDate());

                Time tm = rs.getTime("event_time");
                if (tm != null) t.setEventTime(tm.toLocalTime());

                Timestamp ddl = rs.getTimestamp("registration_deadline");
                if (ddl != null) t.setRegistrationDeadline(ddl.toLocalDateTime());

                t.setMaxParticipants(rs.getInt("max_participants"));
                t.setStatus(rs.getInt("status"));

                // ★現在参加者数：participantsをCOUNTした結果
                t.setParticipantCount(rs.getInt("participant_count"));

                list.add(t);
            }
        }

        return list;
    }

    // 大会IDで1件取得（詳細表示用：全カラム + 参加者数はCOUNTで取得）
    public Tournament findById(int tournamentId) throws SQLException {

        String sql =
            "SELECT " +
            "  t.tournament_id, t.name, t.organizer_id, t.event_date, t.event_time, " +
            "  t.max_participants, t.description, " +
            "  t.venue, t.game_title, t.tournament_format, t.entry_fee_yen, t.registration_deadline, " +
            "  t.match_format, t.time_limit_minutes, t.draw_rule, " +
            "  t.prize_first, t.prize_second, t.prize_third, " +
            "  t.entry_requirement, t.status, t.created_at, t.updated_at, " +
            "  COALESCE(p.cnt, 0) AS participant_count " +
            "FROM tournaments t " +
            "LEFT JOIN ( " +
            "  SELECT tournament_id, COUNT(*) AS cnt " +
            "  FROM participants " +
            "  GROUP BY tournament_id " +
            ") p ON p.tournament_id = t.tournament_id " +
            "WHERE t.tournament_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tournamentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Tournament t = new Tournament();
                t.setTournamentId(rs.getInt("tournament_id"));
                t.setName(rs.getString("name"));
                t.setOrganizerId(rs.getInt("organizer_id"));

                Date d = rs.getDate("event_date");
                if (d != null) t.setEventDate(d.toLocalDate());

                Time tm = rs.getTime("event_time");
                if (tm != null) t.setEventTime(tm.toLocalTime());

                t.setMaxParticipants(rs.getInt("max_participants"));
                t.setDescription(rs.getString("description"));

                t.setVenue(rs.getString("venue"));
                t.setGameTitle(rs.getString("game_title"));
                t.setTournamentFormat(rs.getString("tournament_format"));
                t.setEntryFeeYen(rs.getInt("entry_fee_yen"));

                Timestamp ddl = rs.getTimestamp("registration_deadline");
                if (ddl != null) t.setRegistrationDeadline(ddl.toLocalDateTime());

                t.setMatchFormat(rs.getString("match_format"));
                t.setTimeLimitMinutes(rs.getInt("time_limit_minutes"));
                t.setDrawRule(rs.getString("draw_rule"));

                t.setPrizeFirst(rs.getString("prize_first"));
                t.setPrizeSecond(rs.getString("prize_second"));
                t.setPrizeThird(rs.getString("prize_third"));

                t.setEntryRequirement(rs.getInt("entry_requirement"));
                t.setStatus(rs.getInt("status"));

                Timestamp ca = rs.getTimestamp("created_at");
                if (ca != null) t.setCreatedAt(ca.toLocalDateTime());

                Timestamp ua = rs.getTimestamp("updated_at");
                if (ua != null) t.setUpdatedAt(ua.toLocalDateTime());

                // ★現在参加者数：participantsをCOUNTした結果
                t.setParticipantCount(rs.getInt("participant_count"));

                return t;
            }
        }
    }

    // ★大会ステータス更新（例：開催中=2, 終了=3）
    public int updateStatus(int tournamentId, int status) throws SQLException {

        String sql = "UPDATE tournaments SET status = ?, updated_at = NOW() WHERE tournament_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setInt(2, tournamentId);
            return ps.executeUpdate();
        }
    }
}
