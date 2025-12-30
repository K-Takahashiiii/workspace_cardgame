package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bean.Organizer;
import util.DBUtil;


public class OrganizersDAO {

	//organizersテーブルに関する処理をメソッドにしてまとめるクラス

	//データをRegisterOrganizerServlet.javaから受け取って、
	//organizerテーブルに追加するメソッド
	// 新規登録：登録して、採番された management_num を返す
    public int insertOrganizer(
    		Organizer organizer
    ) throws SQLException {

        String sql =
            "INSERT INTO organizers (store_name, representative_name, password, name) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, organizer.getStoreName());
            ps.setString(2, organizer.getRepresentativeName());
            ps.setString(3, organizer.getPassword());
            ps.setString(4, organizer.getName());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // management_num
                }
            }
        }
        throw new SQLException("management_num の採番取得に失敗しました");
    }


	//LoginOrganizerから情報をもらってテーブルにアカウントが
	//存在するか調べてセッションに登録して返すメソッド
	public Organizer loginCheck(
			int managementNum, String password
	) throws SQLException{

		String sql = "SELECT management_num, store_name, representative_name, password, name " +
	            "FROM organizers " +
	            "WHERE management_num = ? AND password = ?";

		try (Connection con = DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)){

			ps.setInt(1, managementNum);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Organizer o = new Organizer();
                    o.setManagementNum(rs.getInt("management_num"));
                    o.setStoreName(rs.getString("store_name"));
                    o.setRepresentativeName(rs.getString("representative_name"));
                    o.setPassword(rs.getString("password"));
                    o.setName(rs.getString("name"));
                    return o;
                }
            }
		}

		return null;

	}

}
