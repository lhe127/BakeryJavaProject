package bakery;

interface IOrderItem {
    double getTotalPrice();
    String getProductName();
    int getQuantity();
    int getProductId();
}
