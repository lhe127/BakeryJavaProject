package bakery;

import staff.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;

public class BakeryShop extends BaseShop {

    public BakeryShop() {
        super();
        loadProducts();
    }

    @Override
    void loadProducts() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM cakes LIMIT 6";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    products.add(new Products(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            0,
                            rs.getString("imagePath")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    void updateStock(int productId, int quantity) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "UPDATE cakes SET stock = stock - ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, quantity);
                stmt.setInt(2, productId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Products> getProducts() {
        return products;
    }
}
