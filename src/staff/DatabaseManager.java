package staff;

import bakery.Order;
import bakery.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing database interactions related to the bakery system.
 * Provides methods to handle CRUD operations for cakes.
 */
public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sucbakery"; // Replace with your DB details
    private static final String USER = "root";  // Replace with your database username
    private static final String PASSWORD = "58961234";  // Replace with your database password

    /**
     * Establishes a connection to the database.
     *
     * @return a {@link Connection} object for the database.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Inserts a new cake into the database.
     * Ensures that the total number of cakes does not exceed the maximum limit (6).
     *
     * @param name      the name of the cake.
     * @param price     the price of the cake.
     * @param stock     the stock quantity of the cake.
     * @param imagePath the file path to the cake's image.
     * @return {@code true} if the cake was inserted successfully, {@code false} otherwise.
     */
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

    /**
     * Retrieves all cakes from the database.
     *
     * @return a list of cakes, where each cake is represented as an {@code Object[]} containing
     *         the name, price, stock, and image path.
     */
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

    /**
     * Gets the total count of cakes in the database.
     *
     * @return the total number of cakes, or {@code 0} if an error occurs.
     */
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

    /**
     * Updates the details of an existing cake in the database.
     *
     * @param oldName      the current name of the cake to be updated.
     * @param newName      the new name of the cake.
     * @param newPrice     the new price of the cake.
     * @param newStock     the new stock quantity of the cake.
     * @param newImagePath the new image path of the cake.
     * @return {@code true} if the cake was updated successfully, {@code false} otherwise.
     */
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

    /**
     * Deletes a cake from the database based on its name.
     *
     * @param name the name of the cake to be deleted.
     * @return {@code true} if the cake was deleted successfully, {@code false} otherwise.
     */
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

    /**
     * Retrieves a list of orders with detailed information, including customer name, cake details, total items, and more.
     * Orders are sorted by completion time in descending order.
     *
     * @return a list of orders, where each order is represented as an {@code Object[]} containing:
     *         <ul>
     *             <li>Order ID (Integer)</li>
     *             <li>Customer Name (String)</li>
     *             <li>Cake Details (String) - A concatenated string of cake names and quantities</li>
     *             <li>Total Items (Integer)</li>
     *             <li>Total Price (Double)</li>
     *             <li>Donation Amount (Double)</li>
     *             <li>Delivery Address (String)</li>
     *             <li>Completion Time (Timestamp)</li>
     *             <li>Order Status (String)</li>
     *         </ul>
     */
    public static List<Object[]> getOrders() {
        List<Object[]> orders = new ArrayList<>();
        // Modify the query to order by 'order_id' in descending order
        String sql = "SELECT o.id AS order_id, c.name AS customer_name, " +
                "GROUP_CONCAT(CONCAT(ca.name, ' (', oi.quantity, ')') SEPARATOR ', ') AS cake_details, " +
                "SUM(oi.quantity) AS total_items, " +
                "SUM(oi.total_price) AS total_price, " +
                "o.donation_amount, o.delivery_address, o.completion_time, o.status AS order_status " +
                "FROM orderitems oi " +
                "JOIN `order` o ON oi.order_id = o.id " +
                "JOIN customer c ON o.customer_id = c.id " +
                "JOIN cakes ca ON oi.cake_id = ca.id " +
                "GROUP BY o.id, c.name, o.donation_amount, o.delivery_address, o.completion_time, o.status " +
                "ORDER BY completion_time DESC"; // Change to DESC for descending order

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] order = new Object[9]; // Array size to hold order data
                order[0] = rs.getInt("order_id");    // Order ID
                order[1] = rs.getString("customer_name"); // Customer Name
                order[2] = rs.getString("cake_details");   // Concatenated Cake Names and Quantities
                order[3] = rs.getInt("total_items");     // Total Quantity
                order[4] = rs.getDouble("total_price");  // Total Price
                order[5] = rs.getDouble("donation_amount"); // Donation Amount
                order[6] = rs.getString("delivery_address"); // Delivery Address
                order[7] = rs.getTimestamp("completion_time"); // Completion Time
                order[8] = rs.getString("order_status"); // Order Status

                orders.add(order); // Add the order to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL exception for debugging
        }
        return orders;
    }

    /**
     * Updates the status and completion time of a specific order in the database.
     *
     * @param orderId       the ID of the order to be updated.
     * @param status        the new status of the order (e.g., "Completed", "Pending").
     * @param completionTime the completion time to be set for the order.
     */
    public static void updateOrderStatus(int orderId, String status, java.sql.Timestamp completionTime) {
        String sql = "UPDATE `order` SET status = ?, completion_time = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);  // Set the new status
            pstmt.setTimestamp(2, completionTime);  // Set the current time as completion_time
            pstmt.setInt(3, orderId);  // Set the order ID

            pstmt.executeUpdate();  // Execute the update

        } catch (SQLException e) {
            e.printStackTrace();  // Log any SQL exceptions
        }
    }

    /**
     * Retrieves the donation history from the orders, including customer names and donation amounts.
     * Only orders with donation amounts greater than zero are included.
     *
     * @return a list of formatted strings, where each string provides the customer's name and their donation amount.
     *         Example: "John Doe donated RM50.00".
     */
    public static List<String> getDonationHistoryFromOrders() {
        List<String> historyList = new ArrayList<>();
        // SQL query to join `order` with `customer` and fetch the necessary data
        String sql = "SELECT c.name, o.donation_amount FROM `order` o " +
                "INNER JOIN customer c ON o.customer_id = c.id " +
                "WHERE o.donation_amount > 0 " +
                "ORDER BY o.id DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name"); // Get customer's name
                double donationAmount = rs.getDouble("donation_amount");

                // Format donation info
                String donationDetails = String.format("%s donated RM%.2f", name, donationAmount);
                historyList.add(donationDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log error for debugging
        }
        return historyList;
    }

    /**
     * Calculates the total donation amount from all orders.
     * Only orders with donation amounts greater than zero are included in the calculation.
     *
     * @return the total donation amount as a {@code double}.
     */
    public static double getTotalDonationFromOrders() {
        double totalDonation = 0;
        // SQL query to calculate the total donation from the `order` table
        String sql = "SELECT SUM(donation_amount) FROM `order` WHERE donation_amount > 0";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                totalDonation = rs.getDouble(1); // Get the sum of donations
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log error for debugging
        }
        return totalDonation;
    }
}
