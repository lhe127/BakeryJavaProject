package bakery;

public class Products {
    private int id;
    private String productName;
    private double price;
    private int stock;
    private int counter;
    private String image;

    public Products(int id, String productName, double price, int stock, int counter, String image) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.counter = counter;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getImage() {
        return image;
    }
}