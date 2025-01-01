package bakery;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            // Change the URL, username, and password according to your database
            String url = "jdbc:mysql://localhost:3306/sucbakery"; // Database URL
            String username = "root"; // Database username
            String password = "58961234"; // Database password
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
