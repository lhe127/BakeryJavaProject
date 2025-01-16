package bakery;

import java.util.ArrayList;

/**
 * Represents the base class for an order in the bakery system.
 * This class manages the list of order items and the donation amount.
 * It provides an abstract method to calculate the total order amount,
 * which should be implemented by subclasses.
 */
abstract class BaseOrder {

    /** A list of items included in the order. */
    protected ArrayList<OrderItem> items;

    /** The amount donated in the order. */
    protected double donationAmount;

    /**
     * Constructor that initializes an empty list of items and sets the donation amount to 0.
     */
    public BaseOrder() {
        this.items = new ArrayList<>();
        this.donationAmount = 0.0;
    }

    /**
     * Calculates the total amount of the order including the donation.
     * This method must be implemented by subclasses to define the specific
     * calculation logic for the total order amount.
     *
     * @return The total amount of the order.
     */
    abstract double calculateTotal();
}
