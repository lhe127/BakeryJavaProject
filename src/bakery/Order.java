package bakery;

import java.util.ArrayList;

public class Order {
    private ArrayList<OrderItem> items;
    private double donationAmount;

    public Order() {
        this.items = new ArrayList<>();
        this.donationAmount = 0.0;
    }

    public void addItem(Products product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total + donationAmount;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getDonationAmount() {
        return donationAmount;
    }
}