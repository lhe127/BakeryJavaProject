package bakery;

public class Products extends BaseProduct {
    private int counter;

    public Products(int id, String productName, double price, int stock, int counter, String image) {
        super(id, productName, price, stock, image);
        this.counter = counter;
    }

    @Override
    double calculatePrice() {
        return price * counter;
    }

    @Override
    boolean isAvailable() {
        return stock > 0;
    }

    public int getId() { return id; }
    public String getProductName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getCounter() { return counter; }
    public void setCounter(int counter) { this.counter = counter; }
    public String getImage() { return image; }
}