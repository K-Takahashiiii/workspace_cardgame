// organizer-app/src/dao/TournamentsDAO.java
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import bean.Tournament;
import util.DBUtil;

public class TournamentsDAO {

    public void insertTournament(Tournament tournament) throws SQLException {

        String sql = "INSERT INTO tournaments ("
                   + "name, organizer_id, event_date, event_time, "
                   + "max_participants, current_participants, description, status, "
                   + "venue, game_title, tournament_format, entry_fee_yen, registration_deadline, "
                   + "match_format, time_limit_minutes, draw_rule, "
                   + "prize_first, prize_second, prize_third, "
                   + "created_at, updated_at"
                   + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, "
                   + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                   + "NOW(), NOW())";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;

            // 既存
            ps.setString(i++, tournament.getName());
            ps.setInt(i++, tournament.getOrganizerId());
            ps.setObject(i++, tournament.getEventDate());     // LocalDate
            ps.setObject(i++, tournament.getEventTime());     // LocalTime（null可）
            ps.setInt(i++, tournament.getMaxParticipants());
            ps.setInt(i++, tournament.getCurrentParticipants());
            ps.setString(i++, tournament.getDescription());
            ps.setInt(i++, tournament.getStatus());

            // 追加（大会詳細）
            ps.setString(i++, tournament.getVenue());
            ps.setString(i++, tournament.getGameTitle());
            ps.setString(i++, tournament.getTournamentFormat());
            ps.setInt(i++, tournament.getEntryFeeYen());               // 円なので int
            ps.setObject(i++, tournament.getRegistrationDeadline());   // LocalDateTime（null可）

            ps.setString(i++, tournament.getMatchFormat());
            ps.setInt(i++, tournament.getTimeLimitMinutes());
            ps.setString(i++, tournament.getDrawRule());

            ps.setString(i++, tournament.getPrizeFirst());
            ps.setString(i++, tournament.getPrizeSecond());
            ps.setString(i++, tournament.getPrizeThird());

            ps.executeUpdate();
        }
    }

    public List<Tournament> findByOrganizerId(int organizerId) throws SQLException {

        String sql = "SELECT "
                   + "tournament_id, name, organizer_id, event_date, event_time, "
                   + "max_participants, current_participants, description, status, "
                   + "created_at, updated_at "
                   + "FROM tournaments "
                   + "WHERE organizer_id = ? "
                   + "ORDER BY event_date ASC, event_time ASC, tournament_id ASC";

        List<Tournament> list = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, organizerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tournament t = new Tournament();
                    t.setTournamentId(rs.getInt("tournament_id"));
                    t.setName(rs.getString("name"));
                    t.setOrganizerId(rs.getInt("organizer_id"));

                    LocalDate eventDate = rs.getObject("event_date", LocalDate.class);
                    LocalTime eventTime = rs.getObject("event_time", LocalTime.class);
                    LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
                    LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);

                    t.setEventDate(eventDate);
                    t.setEventTime(eventTime);

                    t.setMaxParticipants(rs.getInt("max_participants"));
                    t.setCurrentParticipants(rs.getInt("current_participants"));
                    t.setDescription(rs.getString("description"));
                    t.setStatus(rs.getInt("status"));

                    t.setCreatedAt(createdAt);
                    t.setUpdatedAt(updatedAt);

                    list.add(t);
                }
            }
        }

        return list;
    }
}
