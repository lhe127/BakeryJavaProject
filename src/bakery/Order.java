package bakery;

import java.util.ArrayList;

public class Order extends BaseOrder {

    public Order() {
        super();
    }

    // Implementation of abstract method (Polymorphism)
    @Override
    double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total + donationAmount;
    }

    public void addItem(Products product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    // Encapsulation - getters and setters
    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    public double getTotalAmount() {
        return calculateTotal();
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getDonationAmount() {
        return donationAmount;
    }
}