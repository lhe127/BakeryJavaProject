package staff;

import bakery.Order;
import bakery.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sucbakery"; // Replace with your DB details
    private static final String USER = "root";  // Replace with your database username
    private static final String PASSWORD = "58961234";  // Replace with your database password

    // Establish the connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Insert cake into the database
    public static boolean insertCake(String name, double price, int stock, String imagePath) {
        if (getCakesCount() >= 6) {
            System.out.println("Cannot insert more than 6 cakes.");
            return false; // Restrict insertion if there are already 6 cakes
        }

        String sql = "INSERT INTO cakes (name, price, stock, imagePath) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, stock);
            stmt.setString(4, imagePath);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if insertion failed
        }
    }

    // Retrieve existing cakes
    public static List<Object[]> getCakes() {
        List<Object[]> cakes = new ArrayList<>();
        String sql = "SELECT name, price, stock, imagePath FROM cakes";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Store the cake details in an Object array
                Object[] cake = new Object[4];
                cake[0] = rs.getString("name");     // Cake name
                cake[1] = rs.getDouble("price");    // Cake price
                cake[2] = rs.getInt("stock");       // Cake stock
                cake[3] = rs.getString("imagePath"); // Cake imagePath

                cakes.add(cake); // Add the cake to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cakes;
    }

    // Get the total number of cakes in the database
    public static int getCakesCount() {
        String sql = "SELECT COUNT(*) AS total FROM cakes";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Default to 0 if there's an error
    }

    // Update cake details
    public static boolean updateCake(String oldName, String newName, double newPrice, int newStock, String newImagePath) {
        String sql = "UPDATE cakes SET name = ?, price = ?, stock = ?, imagePath = ? WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setDouble(2, newPrice);
            stmt.setInt(3, newStock);
            stmt.setString(4, newImagePath);
            stmt.setString(5, oldName);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if a row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if update failed
        }
    }
    // Delete cake from the database by name
    public static boolean deleteCake(String name) {
        String sql = "DELETE FROM cakes WHERE name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0; // Return true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if deletion failed
        }
    }

    public static List<Object[]> getOrders() {
        List<Object[]> orders = new ArrayList<>();
        String sql = "SELECT o.id AS order_id, c.name AS customer_name, " +
                "GROUP_CONCAT(CONCAT(ca.name, ' (', oi.quantity, ')') SEPARATOR ', ') AS cake_details, " +
                "SUM(oi.quantity) AS total_items, " +
                "SUM(oi.total_price) AS total_price, " +
                "o.status AS order_status " + // Include the status column
                "FROM orderitems oi " +
                "JOIN `order` o ON oi.order_id = o.id " +
                "JOIN customer c ON o.customer_id = c.id " +
                "JOIN cakes ca ON oi.cake_id = ca.id " +
                "GROUP BY o.id, c.name, o.status " + // Group by status as well
                "ORDER BY o.id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] order = new Object[6]; // Updated array size to include status
                order[0] = rs.getInt("order_id");    // Order ID
                order[1] = rs.getString("customer_name"); // Customer Name
                order[2] = rs.getString("cake_details");   // Concatenated Cake Names and Quantities
                order[3] = rs.getInt("total_items");     // Total Quantity
                order[4] = rs.getDouble("total_price");  // Total Price
                order[5] = rs.getString("order_status"); // Order Status

                orders.add(order); // Add each order's details to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log error for debugging
        }
        return orders;
    }

    public static void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE `order` SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
