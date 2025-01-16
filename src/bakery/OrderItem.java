package bakery;

/**
 * Represents an item in an order.
 * Contains a product and its associated quantity, and provides methods to get item details such as price and quantity.
 */
public class OrderItem {
    private Products product;
    private int quantity;

    /**
     * Constructs a new {@link OrderItem} with the specified product and quantity.
     *
     * @param product The product associated with the order item.
     * @param quantity The quantity of the product in the order item.
     */
    public OrderItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Calculates and returns the total price of the order item, which is the product's price multiplied by the quantity.
     *
     * @return The total price of the order item.
     */
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    /**
     * Returns the name of the product associated with the order item.
     *
     * @return The name of the product.
     */
    public String getProductName() {
        return product.getProductName();
    }

    /**
     * Returns the quantity of the product in the order item.
     *
     * @return The quantity of the product.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the ID of the product associated with the order item.
     *
     * @return The ID of the product.
     */
    public int getProductId() {
        return product.getId();  // Assuming Product class has getId() method
    }
}
