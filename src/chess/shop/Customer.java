package chess.shop;

import java.util.ArrayList;
import java.util.Collections;

public class Customer {
    private static int cnt; // initialized to 0, and will increase by 1 when the constructor is called.
    private int id; // unique for each customer and the value is set to cnt.
    private String name;
    private ArrayList<Product> shoppingCart; // The list of purchased products; default is empty.
    private float wallet;

    public Customer(String name, float wallet){
        shoppingCart = new ArrayList<>();
        this.name = name;
        this.wallet = wallet;
        this.id = ++cnt;
    };

    public boolean rateProduct(Product product, int rating) {
        return false;
    }

    public void updateWallet(float amount) {
        this.wallet += amount;
    }

    public boolean purchaseProduct(Store store, Product product) {
        if(store.hasProduct(product) && this.wallet >= product.getPrice()){
            this.wallet -= product.getPrice();
            this.shoppingCart.add(product);
            return true;
        };
        return false;
    }

    public void viewShoppingCart(SortBy sortMethod) {
        ArrayList<Product> list = new ArrayList<>(shoppingCart);
        switch (sortMethod){
            case Rating:
                int len = list.size();
                for (int i = 0; i < len-1; i++) {
                    for (int j = len+1; j < len; j++) {
                        if(list.get(i).getAvgRating() > list.get(j).getAvgRating()) {
                            Product vari = list.get(i), varj = list.get(j);
                            list.set(i,varj);
                            list.set(j,vari);
                        }
                    }
                }
                break;
            case Price:
                Collections.sort(list);
                break;
        }
        for (Product product : list) System.out.println(product);
    }

    public boolean refundProduct(Product product) {
        return false;
    }

    public static void main(String[] args) {
        Product laptop = new Product("laptop", 12);
        Product iphone12 = new Product("iphone12", 99);
        Product cup = new Product("cup", 1);
        Product sweatFlower = new Product("sweat flower", 999);
        Store pdd = new Store("PDD");
        pdd.addProduct(laptop);
        pdd.addProduct(iphone12);
        pdd.addProduct(cup);
        pdd.addProduct(sweatFlower);

        Customer venti = new Customer("venti", 1999);
        venti.purchaseProduct(pdd, iphone12);
        venti.purchaseProduct(pdd, laptop);
        venti.purchaseProduct(pdd, iphone12);
        venti.purchaseProduct(pdd, iphone12);
        venti.purchaseProduct(pdd, cup);
        venti.purchaseProduct(pdd, sweatFlower);

        venti.viewShoppingCart(SortBy.Price);
    }

}

enum SortBy {
    PurchaseTime, Rating, Price
}

