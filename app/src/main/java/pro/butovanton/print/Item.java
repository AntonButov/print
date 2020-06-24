package pro.butovanton.print;

public class Item {
    String name;
    private Float price;


    public Item(String name, Float price) {
        this.name = name;
        this.setPrice(price);
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
