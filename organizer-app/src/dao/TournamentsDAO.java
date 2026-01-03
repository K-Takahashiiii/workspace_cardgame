package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
