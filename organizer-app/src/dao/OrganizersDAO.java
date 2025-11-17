package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Organizer;
import util.DBUtil;


public class OrganizersDAO {

	//organizersテーブルに関する処理をメソッドにしてまとめるクラス

	//データをRegisterOrganizerServlet.javaから受け取って、
	//organizerテーブルに追加するメソッド
	public void insertOrganizers(
			String name, String pass, int tellNum
	) throws SQLException{

		//SQL文設定
		//try文で次の処理を実行
		//データベースに接続(conとしてDButil.getConnection()をインスタンス化)
		//SQL文をcon.prepareStatement()に渡してpsとしてインスタンス化
		//各値をps.serString()等のメソッドでで設定する
		//ps.executeUpdate()でSQL文の実行
		//try文から抜けてホーム画面のリンクにフォワード
		//例外が発生したらcatchでエラーが発生したら、
		//"エラーが発生しました"error変数の値をrequestに
		//持たせて新規登録のjspにフォワード

		String sql = "insert into organizers(name, pass, tell_num)"
				 + ( "values(?, ?, ?)");

		try(Connection con =  DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)){

			ps.setString(1, name);
			ps.setString(2, pass);
			ps.setInt(3, tellNum);

			ps.executeUpdate();

		}//例外が起きるとエラーを呼び出し元(RegisterOrganizerServlet)に投げる
	}


	//LoginOrganizerから情報をもらってテーブルにアカウントが
	//存在するか調べてセッションに登録して返すメソッド
	public Organizer loginCheck(
			String name, String pass
	) throws SQLException{

		String sql = "SELECT id, name, pass, tell_num "
				+ "FROM organizers "
				+ "WHERE name = ? AND pass = ?";

		try (Connection con = DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)){

			ps.setString(1, name);
			ps.setString(2, pass);

			ResultSet rs =  ps.executeQuery();

			if(rs.next()){
				String id = String.valueOf(rs.getInt("id"));
				String dbName = rs.getString("name");
				String dbPass = rs.getString("pass");
				String tellNum = String.valueOf(rs.getInt("tell_num"));

				return new Organizer(id, dbName, dbPass, tellNum);
			}
		}

		return null;

	}
	
	
	
	public void logout() {
		
	}
	
}
