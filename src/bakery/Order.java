package bakery;

import java.util.ArrayList;

/**
 * Represents an order in the bakery system.
 * Extends from {@link BaseOrder} and implements the method to calculate the total order cost.
 * This class also provides functionality to add items to the order and manage donation amounts.
 */
public class Order extends BaseOrder {

    /**
     * Constructs a new {@link Order} object with an empty list of items and a donation amount set to 0.0.
     */
    public Order() {
        super();
    }

    /**
     * Calculates the total cost of the order, including the total price of all items and any donation amount.
     *
     * @return The total cost of the order.
     */
    // Implementation of abstract method (Polymorphism)
    @Override
    double calculateTotal() {
        double total = 0.0;
        // Summing up the total price of all items in the order
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        // Adding donation amount to the total
        return total + donationAmount;
    }

    /**
     * Adds an item to the order.
     *
     * @param product The product to be added to the order.
     * @param quantity The quantity of the product to be added.
     */
    public void addItem(Products product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    /**
     * Sets the donation amount for the order.
     *
     * @param donationAmount The amount to be donated.
     */
    // Encapsulation - getters and setters
    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    /**
     * Gets the total amount of the order, which includes both the item prices and donation amount.
     *
     * @return The total amount of the order.
     */
    public double getTotalAmount() {
        return calculateTotal();
    }

    /**
     * Gets the list of items in the order.
     *
     * @return A list of {@link OrderItem} objects representing the items in the order.
     */
    public ArrayList<OrderItem> getItems() {
        return items;
    }

    /**
     * Gets the donation amount associated with the order.
     *
     * @return The donation amount for the order.
     */
    public double getDonationAmount() {
        return donationAmount;
    }
}