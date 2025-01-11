package bakery;

public class OrderItem implements IOrderItem {
    private Products product;
    private int quantity;

    public OrderItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String getProductName() {
        return product.getProductName();
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public int getProductId() {
        return product.getId();
    }
}
