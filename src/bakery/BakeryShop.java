package bakery;

import staff.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;

/**
 * Represents a Bakery Shop that extends from a base shop.
 * Provides functionality to load bakery products, update stock, and retrieve products.
 */
public class BakeryShop extends BaseShop {

    /**
     * Constructor that initializes the BakeryShop by loading the products.
     */
    public BakeryShop() {
        super();
        loadProducts();
    }

    /**
     * Loads bakery products from the database (limited to the first 6 products) and adds them to the product list.
     * The product information is fetched from the 'cakes' table in the database.
     */
    @Override
    void loadProducts() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM cakes LIMIT 6"; //Query to fetch up to 6 cakes
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

    /**
     * Updates the stock of a product by reducing its quantity.
     * This method is used when an order is placed to reduce the available stock of the selected product.
     *
     * @param productId The ID of the product to update the stock for.
     * @param quantity  The quantity to reduce from the current stock.
     */
    @Override
    void updateStock(int productId, int quantity) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "UPDATE cakes SET stock = stock - ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, quantity); // Set the quantity to reduce
                stmt.setInt(2, productId); // Set the product ID
                stmt.executeUpdate(); // Execute the update query
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of products in the bakery shop.
     *
     * @return An {@link ArrayList} containing {@link Products} objects in the bakery shop.
     */
    public ArrayList<Products> getProducts() {
        return products;
    }
}
