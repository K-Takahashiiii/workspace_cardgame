package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBUtil;

public class ParticipantsDAO {

    /** 既にエントリー済みか */
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

    /** 指定した大会の参加者数を返す */
    public int countByTournamentId(int tournamentId) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM participants WHERE tournament_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
                return 0;
            }
        }
    }

    /**
     * 戻り値:
     *  1 = 成功
     *  0 = 既に参加済み
     * -1 = 定員オーバー
     *
     * 前提:
     *  participants に UNIQUE(tournament_id, player_id) があること
     */
    public int enterWithCountUp(int tournamentId, int playerId, String playerName) throws SQLException {

        String lockTournamentSql =
            "SELECT max_participants FROM tournaments WHERE tournament_id = ? FOR UPDATE";

        String countSql =
            "SELECT COUNT(*) AS cnt FROM participants WHERE tournament_id = ?";

        String insertSql =
            "INSERT INTO participants (tournament_id, player_id, player_name) VALUES (?, ?, ?)";

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            try {
                // (A) 大会行をロックして定員取得
                int maxParticipants;
                try (PreparedStatement ps = con.prepareStatement(lockTournamentSql)) {
                    ps.setInt(1, tournamentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Tournament not found: tournament_id=" + tournamentId);
                        }
                        maxParticipants = rs.getInt("max_participants");
                    }
                }

                // (B) 既に参加済みなら0
                if (existsTx(con, tournamentId, playerId)) {
                    con.rollback();
                    return 0;
                }

                // (C) 参加者数COUNTで定員判定
                int currentCount;
                try (PreparedStatement ps = con.prepareStatement(countSql)) {
                    ps.setInt(1, tournamentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        currentCount = rs.getInt("cnt");
                    }
                }

                if (currentCount >= maxParticipants) {
                    con.rollback();
                    return -1;
                }

                // (D) INSERT
                try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                    ins.setInt(1, tournamentId);
                    ins.setInt(2, playerId);
                    ins.setString(3, playerName);
                    ins.executeUpdate();
                } catch (SQLException e) {
                    if (isDuplicateKey(e)) {
                        con.rollback();
                        return 0;
                    }
                    throw e;
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

    private boolean existsTx(Connection con, int tournamentId, int playerId) throws SQLException {
        String sql = "SELECT 1 FROM participants WHERE tournament_id = ? AND player_id = ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ps.setInt(2, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean isDuplicateKey(SQLException e) {
        if (e.getErrorCode() == 1062) return true;
        String state = e.getSQLState();
        return state != null && state.startsWith("23");
    }
}
