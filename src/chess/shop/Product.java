package chess.shop;

import chess.style.Style;

import java.util.ArrayList;

public class Product implements Comparable<Product>{
    private static int cnt;
    private int id;
    private String name;
    private int price;
    private ArrayList<Integer> ratings=new ArrayList<>();
    private int storeID=0;
    public Style style;

    public Product(String name, int price){
        cnt++;
        this.name=name;
        this.price=price;
        this.id=cnt;
    }

    public Product(String name, int price, Style style){
        cnt++;
        this.name=name;
        this.price=price;
        this.id=cnt;
        this.style = style;
    }

    public int getID(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public int getPrice(){
        return this.price;
    }

    public void setStoreID(int storeID){
        this.storeID=storeID;
    }
    public int getStoreID(){
        return this.storeID;
    }

    public boolean setRating(int rating){
        if (rating==1||rating==2||rating==3||rating==4||rating==5) {
            this.ratings.add(rating);
            return true;
        }
        return false;
    }



    public float getAvgRating(){
        float all=0;
        if (this.ratings.size()==0) return 0f;
        if (this.ratings.size()==1) return this.ratings.get(0);
        for (int i=0;i<this.ratings.size();i++){
            all=all+this.ratings.get(i);
        }
        return all/(this.ratings.size());
    }

    public boolean canBuy(int price){
        return this.price <= price;
    }

    @Override
    public String toString(){
        return String.format("Product ID %d, %s, RMB %d, Rating %d",this.id,this.name,this.price,this.getAvgRating());
    }

    @Override
    public int compareTo(Product o) {
        return this.price - o.price;
    }
}