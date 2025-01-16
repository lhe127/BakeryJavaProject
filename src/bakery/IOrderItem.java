package bakery;

/**
 * Represents an interface for an order item in the bakery system.
 * This interface defines the methods that must be implemented by any class
 * that represents an item in an order.
 */
interface IOrderItem {

    /**
     * Gets the total price of the order item.
     *
     * @return The total price of the order item, calculated as quantity multiplied by product price.
     */
    double getTotalPrice();

    /**
     * Gets the name of the product associated with this order item.
     *
     * @return The name of the product.
     */
    String getProductName();

    /**
     * Gets the quantity of the product in the order item.
     *
     * @return The quantity of the product.
     */
    int getQuantity();


    /**
     * Gets the unique identifier of the product associated with this order item.
     *
     * @return The product's unique identifier (ID).
     */
    int getProductId();
}
