package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // 共通の接続パラメータ（コピペ防止）
    private static final String PARAMS =
        "?useSSL=false"
      + "&allowPublicKeyRetrieval=true"
      + "&serverTimezone=Asia/Tokyo"
      + "&characterEncoding=UTF-8";

    // 宮っさんのメインPC（共有DB）
    private static final String REMOTE_URL  =
        "jdbc:mysql://10.251.197.147:3306/card_app" + PARAMS;
    private static final String REMOTE_USER = "card_team";
    private static final String REMOTE_PASS = "rootpass";

    // 自分のPCのローカルDB（家で開発用）
    private static final String LOCAL_URL  =
        "jdbc:mysql://localhost:3306/tournament_app" + PARAMS;
    private static final String LOCAL_USER = "root";      // 自分のローカルMySQLユーザ
    private static final String LOCAL_PASS = "rootpass";  // そのパス

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBCドライバのロードに失敗しました", e);
        }
    }

    public static Connection getConnection() throws SQLException {

        // ① まずメインPCに接続トライ
        try {
            return DriverManager.getConnection(REMOTE_URL, REMOTE_USER, REMOTE_PASS);
        } catch (SQLException e) {
            System.err.println("[DBUtil] remote接続失敗 → localに切り替え: " + e.getMessage());
        }

        // ② ダメだったら localhost に接続トライ
        return DriverManager.getConnection(LOCAL_URL, LOCAL_USER, LOCAL_PASS);
    }
}
