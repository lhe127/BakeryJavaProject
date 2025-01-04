package bakery;

import staff.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;

public class BakeryShop {

    public ArrayList<Products> products = new ArrayList<Products>();

    public BakeryShop() {
        // Get products from the database
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM cakes LIMIT 6"; // Retrieve 6 products from the database
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id"); // Assuming 'id' is the primary key in your 'cakes' table
                    String productName = rs.getString("name");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    String image = rs.getString("imagePath");

                    // Add products to the list, including the 'id'
                    products.add(new Products(id, productName, price, stock, 0, image));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
