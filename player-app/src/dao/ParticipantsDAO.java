package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBUtil;

public class ParticipantsDAO {

    public boolean exists(int tournamentId, int playerId) throws SQLException {
        String sql = "SELECT 1 FROM participants WHERE tournament_id = ? AND player_id = ? LIMIT 1";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ps.setInt(2, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * 戻り値:
     *  1 = 成功
     *  0 = 既に参加済み
     * -1 = 定員オーバー
     */
    public int enterWithCountUp(int tournamentId, int playerId, String playerName) throws SQLException {

        String insertSql =
            "INSERT INTO participants (tournament_id, player_id, player_name) VALUES (?, ?, ?)";

        String updateSql =
            "UPDATE tournaments " +
            "SET current_participants = current_participants + 1 " +
            "WHERE tournament_id = ? AND current_participants < max_participants";

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ins = con.prepareStatement(insertSql);
                 PreparedStatement upd = con.prepareStatement(updateSql)) {

                // 1) participantsへINSERT（重複はUNIQUEで弾く）
                try {
                    ins.setInt(1, tournamentId);
                    ins.setInt(2, playerId);
                    ins.setString(3, playerName);
                    ins.executeUpdate();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1062) { // duplicate key
                        con.rollback();
                        return 0;
                    }
                    throw e;
                }

                // 2) 定員チェック込みで +1
                upd.setInt(1, tournamentId);
                int updated = upd.executeUpdate();
                if (updated == 0) {
                    con.rollback(); // INSERTも取り消す
                    return -1;
                }

                con.commit();
                return 1;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
