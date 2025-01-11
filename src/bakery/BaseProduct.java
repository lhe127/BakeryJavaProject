package bakery;

abstract class BaseProduct {
    protected int id;
    protected String name;
    protected double price;
    protected int stock;
    protected String image;

    public BaseProduct(int id, String name, double price, int stock, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    abstract double calculatePrice();
    abstract boolean isAvailable();
}
