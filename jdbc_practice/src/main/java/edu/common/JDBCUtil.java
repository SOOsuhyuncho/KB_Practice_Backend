package edu.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtil {

    private static Connection conn = null;

    static {
        try {
            Properties properties = new Properties();
            // 최상위 경로(/)에 있는 application.properties 파일을 읽어옵니다.
            properties.load(JDBCUtil.class.getResourceAsStream("/application.properties"));

            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String id = properties.getProperty("edu.id");
            String password = properties.getProperty("edu.pw");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, id, password);
            conn.setAutoCommit(false); // 수동 커밋 설정 (데이터 수정 시 필수)

        } catch (Exception e) {
            System.out.println("[DB 연결 실패] 설정 파일이나 DB 상태를 확인해주세요.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    // DB 연결 종료 (프로그램 종료 시 사용)
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Statement / PreparedStatement 반환용
    public static void close(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ResultSet 반환용
    public static void close(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터 저장 확정 (INSERT, UPDATE, DELETE 시 사용)
    public static void commit(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터 저장 취소
    public static void rollback(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}