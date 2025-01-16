package bakery;

/**
 * Represents the base class for a product in the bakery system.
 * This class holds the product details and provides abstract methods
 * to calculate the price and check availability.
 */
abstract class BaseProduct {

    /** The unique identifier of the product. */
    protected int id;

    /** The name of the product. */
    protected String name;

    /** The price of the product. */
    protected double price;

    /** The stock quantity of the product. */
    protected int stock;

    /** The image path or URL associated with the product. */
    protected String image;

    /**
     * Constructs a new product with the specified details.
     *
     * @param id The unique identifier of the product.
     * @param name The name of the product.
     * @param price The price of the product.
     * @param stock The stock quantity of the product.
     * @param image The image path or URL associated with the product.
     */
    public BaseProduct(int id, String name, double price, int stock, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    /**
     * Calculates the price of the product.
     * This method must be implemented by subclasses to define the specific
     * logic for calculating the price.
     *
     * @return The calculated price of the product.
     */
    abstract double calculatePrice();

    /**
     * Checks if the product is available in stock.
     * This method must be implemented by subclasses to define the specific
     * logic for determining availability.
     *
     * @return {@code true} if the product is available in stock, otherwise {@code false}.
     */
    abstract boolean isAvailable();
}
