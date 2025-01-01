package bakery;

import java.sql.*;
import java.util.ArrayList;

public class BakeryShop {

    public ArrayList<Products> products = new ArrayList<Products>();

    public BakeryShop() {
        // Get products from the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM cakes LIMIT 6"; // Retrieve 6 products from the database
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String productName = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String image = rs.getString("imagePath");

                products.add(new Products(productName, price, stock, 0, image));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

