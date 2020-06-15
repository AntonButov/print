package pro.butovanton.print;

public class Order {
    public String tel;
    public String size;
    public String quality;

    @Override
    public String toString() {
        return "Order{" +
                "tel='" + tel + '\'' +
                ", size='" + size + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }
}
