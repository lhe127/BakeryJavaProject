package bakery;

import java.util.ArrayList;

/**
 * Represents the base class for a shop in the bakery system.
 * This class holds the list of products and defines abstract methods
 * for loading products and updating stock.
 */
abstract class BaseShop {

    /** A list that holds all the products available in the shop. */
    protected ArrayList<Products> products;

    /**
     * Constructs a new shop and initializes the list of products.
     */
    public BaseShop() {
        this.products = new ArrayList<>();
    }

    /**
     * Loads the products into the shop.
     * This method must be implemented by subclasses to define how products are loaded
     * into the shop (e.g., from a database, API, or file).
     */
    // Abstract methods
    abstract void loadProducts();

    /**
     * Updates the stock quantity of a specific product in the shop.
     * This method must be implemented by subclasses to define how stock is updated
     * for a given product.
     *
     * @param productId The unique identifier of the product whose stock needs to be updated.
     * @param quantity The quantity to update the stock by (positive or negative).
     */
    abstract void updateStock(int productId, int quantity);
}
