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

    // INSERT（entry_requirement 追加）
    public void insertTournament(Tournament tournament) throws SQLException {

        String sql = "INSERT INTO tournaments ("
                   + "name, organizer_id, event_date, event_time, "
                   + "max_participants, current_participants, description, status, "
                   + "venue, game_title, tournament_format, entry_fee_yen, registration_deadline, "
                   + "match_format, time_limit_minutes, draw_rule, "
                   + "prize_first, prize_second, prize_third, "
                   + "entry_requirement, "
                   + "created_at, updated_at"
                   + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, "
                   + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
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

            // 今回追加（単数）
            ps.setInt(i++, tournament.getEntryRequirement());          // 0=条件なし運用

            ps.executeUpdate();
        }
    }

    // 一覧（現状維持：必要最小限）
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

    // 設定画面用：大会IDで1件取得（全カラム）
    public Tournament findByTournamentId(int tournamentId) throws SQLException {

        String sql = "SELECT "
                   + "tournament_id, name, organizer_id, event_date, event_time, "
                   + "max_participants, current_participants, description, status, "
                   + "venue, game_title, tournament_format, entry_fee_yen, registration_deadline, "
                   + "match_format, time_limit_minutes, draw_rule, "
                   + "prize_first, prize_second, prize_third, "
                   + "entry_requirement, "
                   + "created_at, updated_at "
                   + "FROM tournaments "
                   + "WHERE tournament_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tournamentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Tournament t = new Tournament();

                t.setTournamentId(rs.getInt("tournament_id"));
                t.setName(rs.getString("name"));
                t.setOrganizerId(rs.getInt("organizer_id"));

                t.setEventDate(rs.getObject("event_date", LocalDate.class));
                t.setEventTime(rs.getObject("event_time", LocalTime.class));

                t.setMaxParticipants(rs.getInt("max_participants"));
                t.setCurrentParticipants(rs.getInt("current_participants"));
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getInt("status"));

                t.setVenue(rs.getString("venue"));
                t.setGameTitle(rs.getString("game_title"));
                t.setTournamentFormat(rs.getString("tournament_format"));
                t.setEntryFeeYen(rs.getInt("entry_fee_yen"));
                t.setRegistrationDeadline(rs.getObject("registration_deadline", LocalDateTime.class));

                t.setMatchFormat(rs.getString("match_format"));
                t.setTimeLimitMinutes(rs.getInt("time_limit_minutes"));
                t.setDrawRule(rs.getString("draw_rule"));

                t.setPrizeFirst(rs.getString("prize_first"));
                t.setPrizeSecond(rs.getString("prize_second"));
                t.setPrizeThird(rs.getString("prize_third"));

                // 今回追加（単数）
                t.setEntryRequirement(rs.getInt("entry_requirement"));

                t.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                t.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));

                return t;
            }
        }
    }

    // 設定画面用：詳細一式を更新（entry_requirement も更新対象）
    // organizer_id もWHEREに入れて、他人の大会更新を防ぐ
    public int updateTournament(Tournament t) throws SQLException {

        String sql = "UPDATE tournaments SET "
                   + "name=?, event_date=?, event_time=?, max_participants=?, description=?, status=?, "
                   + "venue=?, game_title=?, tournament_format=?, entry_fee_yen=?, registration_deadline=?, "
                   + "match_format=?, time_limit_minutes=?, draw_rule=?, "
                   + "prize_first=?, prize_second=?, prize_third=?, "
                   + "entry_requirement=?, "
                   + "updated_at=NOW() "
                   + "WHERE tournament_id=? AND organizer_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;

            ps.setString(i++, t.getName());
            ps.setObject(i++, t.getEventDate());
            ps.setObject(i++, t.getEventTime());
            ps.setInt(i++, t.getMaxParticipants());
            ps.setString(i++, t.getDescription());
            ps.setInt(i++, t.getStatus());

            ps.setString(i++, t.getVenue());
            ps.setString(i++, t.getGameTitle());
            ps.setString(i++, t.getTournamentFormat());
            ps.setInt(i++, t.getEntryFeeYen());
            ps.setObject(i++, t.getRegistrationDeadline());

            ps.setString(i++, t.getMatchFormat());
            ps.setInt(i++, t.getTimeLimitMinutes());
            ps.setString(i++, t.getDrawRule());

            ps.setString(i++, t.getPrizeFirst());
            ps.setString(i++, t.getPrizeSecond());
            ps.setString(i++, t.getPrizeThird());

            ps.setInt(i++, t.getEntryRequirement());

            ps.setInt(i++, t.getTournamentId());
            ps.setInt(i++, t.getOrganizerId());

            return ps.executeUpdate();
        }
    }
}
