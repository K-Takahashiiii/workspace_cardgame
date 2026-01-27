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

    // プレイヤー側：大会一覧（必要最低限だけ）
    public List<Tournament> findAll() throws SQLException {

        String sql =
            "SELECT tournament_id, name, venue, entry_requirement, " +
            "       event_date, event_time, registration_deadline, " +
            "       max_participants, current_participants, status " +
            "FROM tournaments " +
            "ORDER BY tournament_id DESC";

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
                t.setCurrentParticipants(rs.getInt("current_participants"));
                t.setStatus(rs.getInt("status"));

                list.add(t);
            }
        }

        return list;
    }



    // 追加：大会IDで1件取得（詳細表示用）
    public Tournament findById(int tournamentId) throws SQLException {
        String sql =
            "SELECT tournament_id, name, organizer_id, event_date, event_time, " +
            "       max_participants, current_participants, description, " +
            "       venue, game_title, tournament_format, entry_fee_yen, registration_deadline, " +
            "       match_format, time_limit_minutes, draw_rule, " +
            "       prize_first, prize_second, prize_third, " +
            "       entry_requirement, status, created_at, updated_at " +
            "FROM tournaments WHERE tournament_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tournamentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Tournament t = new Tournament();
                t.setTournamentId(rs.getInt("tournament_id"));
                t.setName(rs.getString("name"));
                t.setOrganizerId(rs.getInt("organizer_id"));

                java.sql.Date d = rs.getDate("event_date");
                if (d != null) t.setEventDate(d.toLocalDate());

                java.sql.Time tm = rs.getTime("event_time");
                if (tm != null) t.setEventTime(tm.toLocalTime());

                t.setMaxParticipants(rs.getInt("max_participants"));
                t.setCurrentParticipants(rs.getInt("current_participants"));
                t.setDescription(rs.getString("description"));

                t.setVenue(rs.getString("venue"));
                t.setGameTitle(rs.getString("game_title"));
                t.setTournamentFormat(rs.getString("tournament_format"));
                t.setEntryFeeYen(rs.getInt("entry_fee_yen"));

                java.sql.Timestamp ddl = rs.getTimestamp("registration_deadline");
                if (ddl != null) t.setRegistrationDeadline(ddl.toLocalDateTime());

                t.setMatchFormat(rs.getString("match_format"));
                t.setTimeLimitMinutes(rs.getInt("time_limit_minutes"));
                t.setDrawRule(rs.getString("draw_rule"));

                t.setPrizeFirst(rs.getString("prize_first"));
                t.setPrizeSecond(rs.getString("prize_second"));
                t.setPrizeThird(rs.getString("prize_third"));

                t.setEntryRequirement(rs.getInt("entry_requirement"));
                t.setStatus(rs.getInt("status"));

                java.sql.Timestamp ca = rs.getTimestamp("created_at");
                if (ca != null) t.setCreatedAt(ca.toLocalDateTime());

                java.sql.Timestamp ua = rs.getTimestamp("updated_at");
                if (ua != null) t.setUpdatedAt(ua.toLocalDateTime());

                return t;
            }
        }
    }





}
