package bakery;

/**
 * Represents a product in the bakery, extending {@link BaseProduct}.
 * This class includes additional attributes and methods for managing the product's stock, price, and other related information.
 */
public class Products extends BaseProduct {
    private int counter;

    /**
     * Constructs a new {@link Products} object with the specified attributes.
     *
     * @param id The unique identifier for the product.
     * @param productName The name of the product.
     * @param price The price of the product.
     * @param stock The stock quantity available for the product.
     * @param counter The counter representing the number of units of the product.
     * @param image The image path associated with the product.
     */
    public Products(int id, String productName, double price, int stock, int counter, String image) {
        super(id, productName, price, stock, image);
        this.counter = counter;
    }

    /**
     * Calculates the total price for the product, which is the price of the product multiplied by the counter.
     *
     * @return The total price for the product.
     */
    @Override
    double calculatePrice() {
        return price * counter;
    }

    @Override
    boolean isAvailable() {
        return stock > 0;
    }

    /**
     * Returns the ID of the product.
     *
     * @return The product's unique ID.
     */
    public int getId() { return id; }

    /**
     * Returns the name of the product.
     *
     * @return The name of the product.
     */
    public String getProductName() { return name; }

    /**
     * Returns the price of the product.
     *
     * @return The price of the product.
     */
    public double getPrice() { return price; }

    /**
     * Returns the stock quantity of the product.
     *
     * @return The stock quantity of the product.
     */
    public int getStock() { return stock; }

    /**
     * Returns the counter for the product, representing the number of units of the product.
     *
     * @return The counter for the product.
     */
    public int getCounter() { return counter; }

    /**
     * Sets the counter for the product, representing the number of units of the product.
     *
     * @param counter The new counter value for the product.
     */
    public void setCounter(int counter) { this.counter = counter; }

    /**
     * Returns the image associated with the product.
     *
     * @return The image path of the product.
     */
    public String getImage() { return image; }
}