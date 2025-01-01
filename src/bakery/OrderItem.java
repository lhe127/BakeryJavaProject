package bakery;

public class OrderItem {
    private Products product;
    private int quantity;

    public OrderItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    public String getProductName() {
        return product.getProductName();
    }

    public int getQuantity() {
        return quantity;
    }
}
