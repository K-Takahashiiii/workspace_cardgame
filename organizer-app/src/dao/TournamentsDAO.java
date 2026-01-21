// organizer-app/src/dao/TournamentsDAO.java  ※追記版（findByOrganizerId を追加）
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
                   + "created_at, updated_at"
                   + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tournament.getName());
            ps.setInt(2, tournament.getOrganizerId());
            ps.setObject(3, tournament.getEventDate());   // LocalDate
            ps.setObject(4, tournament.getEventTime());   // LocalTime（null可）
            ps.setInt(5, tournament.getMaxParticipants());
            ps.setInt(6, tournament.getCurrentParticipants());
            ps.setString(7, tournament.getDescription());
            ps.setInt(8, tournament.getStatus());

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
