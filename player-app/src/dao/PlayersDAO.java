package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bean.Player;
import util.DBUtil;

public class PlayersDAO {

    // 新規登録：登録して、採番された USER_NUM を返す
    public int insertPlayer(Player player) throws SQLException {
        String sql = "INSERT INTO players (TELEPHONE_NUM, `NAME`, `PASSWORD`, PLAYER_NAME) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, player.getTelephoneNum());
            ps.setString(2, player.getName());
            ps.setString(3, player.getPassword());
            ps.setString(4, player.getPlayerName());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insert failed: no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new SQLException("Insert failed: no generated key returned.");
        }
    }

    // ログイン：電話番号＋パスワード一致で Player を返す（失敗なら null）
    public Player findByTelephoneAndPassword(String telephoneNum, String password) throws SQLException {
        String sql = "SELECT USER_NUM, TELEPHONE_NUM, `NAME`, `PASSWORD`, PLAYER_NAME "
                   + "FROM players "
                   + "WHERE TELEPHONE_NUM = ? AND `PASSWORD` = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, telephoneNum);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Player p = new Player();
                    p.setUserNum(rs.getInt("USER_NUM"));
                    p.setTelephoneNum(rs.getString("TELEPHONE_NUM"));
                    p.setName(rs.getString("NAME"));
                    p.setPassword(rs.getString("PASSWORD"));
                    p.setPlayerName(rs.getString("PLAYER_NAME"));
                    return p;
                }
                return null;
            }
        }
    }

    // （呼び出し側がまだ insertOrganizer を使ってる場合の互換）
    public int insertOrganizer(Player player) throws SQLException {
        return insertPlayer(player);
    }
}
