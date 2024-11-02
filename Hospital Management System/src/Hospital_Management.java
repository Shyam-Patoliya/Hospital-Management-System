import java.sql.*;

public class Hospital_Management {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/Hospital";
    static final String USER = "root";
    static final String PASS = "463922";
    static Connection conn = null;
    static Statement stmt = null;

    public static void main(String[] args) {
        try {
            createTables();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    static void createTables() throws SQLException {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS doctors (" +
                    "d_id INT PRIMARY KEY," +
                    "d_name VARCHAR(25)," +
                    "d_age INT," +
                    "d_department VARCHAR(20)," +
                    "d_phono BIGINT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patients (" +
                    "p_id INT PRIMARY KEY," +
                    "p_name VARCHAR(25)," +
                    "p_age INT," +
                    "p_problems VARCHAR(50)," +
                    "p_phono BIGINT," +
                    "p_admitdate DATE," +
                    "d_id INT," +
                    "FOREIGN KEY (d_id) REFERENCES doctors(d_id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS workers (" +
                    "w_id INT PRIMARY KEY," +
                    "w_name VARCHAR(25)," +
                    "w_age INT," +
                    "w_workname VARCHAR(50)," +
                    "w_phono BIGINT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bills (" +
                    "bill_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "p_id INT," +
                    "d_id INT," +
                    "p_drvisit DECIMAL(10, 2)," +
                    "p_medicines DECIMAL(10, 2)," +
                    "p_room DECIMAL(10, 2)," +
                    "total_bill DECIMAL(10, 2) DEFAULT 0," +
                    "FOREIGN KEY (p_id) REFERENCES patients(p_id)," +
                    "FOREIGN KEY (d_id) REFERENCES doctors(d_id))");

            System.out.println("Tables created successfully");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
