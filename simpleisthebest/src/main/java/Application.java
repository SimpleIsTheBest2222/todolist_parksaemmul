import com.smpark.jdbc.config.config.JDBCConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        try {
            Connection connection = JDBCConnection.getConnection();
            System.out.println("연결 성공: " + connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}