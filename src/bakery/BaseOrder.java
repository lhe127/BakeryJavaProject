package bakery;

import java.util.ArrayList;

abstract class BaseOrder {
    protected ArrayList<OrderItem> items;
    protected double donationAmount;

    public BaseOrder() {
        this.items = new ArrayList<>();
        this.donationAmount = 0.0;
    }

    abstract double calculateTotal();
}
