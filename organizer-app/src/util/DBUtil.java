package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // DB接続情報（自分の環境に合わせて変更）
    private static final String URL =
        "jdbc:mysql://localhost:3306/tournament_app"
      + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Tokyo&characterEncoding=UTF-8";

    private static final String USER = "root";       // ★ここを自分のユーザ名に
    private static final String PASS = "ROOTPASS";  // ★ここを自分のパスワードに

    // JDBCドライバのロード（クラス読み込み時に1回だけ実行）
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8 用
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBCドライバのロードに失敗しました", e);
        }
    }

    // コネクション取得用メソッド
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
