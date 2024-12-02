package bakery;

import java.util.ArrayList;

public class BakeryShop {
    ArrayList<Products> products = new ArrayList<Products>();

    public BakeryShop() {
        products.add(new Products("Chocolate Cake", 20.99, 50, 0, "src/img/Name1.jpeg"));
        products.add(new Products("Cream Cake", 14.99, 50, 0, "src/img/Name2.jpeg"));
        products.add(new Products("Crystel's face", 25.99, 50, 0, "src/img/Name3.jpeg"));
        products.add(new Products("Birthday Cake", 20.99, 50, 0, "src/img/Name4.jpeg"));
        products.add(new Products("Love Cake", 14.99, 50, 0, "src/img/Name5.jpeg"));
        products.add(new Products("Death Cake", 25.99, 50, 0, "src/img/Name6.jpeg"));
    }
}
