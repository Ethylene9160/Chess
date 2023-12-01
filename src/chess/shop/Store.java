package chess.shop;

import java.util.ArrayList;

public class Store {

    private static int cnt; // initialized to 0, and will increase by 1 when the constructor is called.
    private int id; // unique for each store and the value is set to cnt.
    private String name;
    private ArrayList<Product> productList;
    private float income;

    public Store(String name){
        this(name, new ArrayList<>(), 0.0f);
    };
    public Store(String name, ArrayList<Product> productList, float income){
        this.name = name;
        this.income = income;
        this.productList = productList;
    };

    public boolean hasProduct(Product product){
        for (Product product1 : productList) {
            if(product1.getID()==product.getID())return true;
        }
        return false;
    };
    public boolean addProduct(Product product){
        if(!hasProduct(product)) {
            productList.add(product);
            return true;
        }
        return false;
    };
    public boolean removeProduct(Product product){
        if(hasProduct(product)) {
            productList.remove(product);
            return true;
        }
        return false;
    };

    public ArrayList<Product> getProductList(){
        return this.productList;
    };
    public void transact(Product product, int method){

    };
}
