package bakery;

import java.util.ArrayList;

abstract class BaseShop {
    protected ArrayList<Products> products;

    public BaseShop() {
        this.products = new ArrayList<>();
    }

    // Abstract methods
    abstract void loadProducts();
    abstract void updateStock(int productId, int quantity);
}
