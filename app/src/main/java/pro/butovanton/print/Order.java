package pro.butovanton.print;

public class Order {
    public String tel;
    public String size;
    public String quality;
    public int num;

    public Order() {
        num = 1;
    }

    @Override
    public String toString() {
        return "Order{" +
                "tel='" + tel + '\'' +
                ", size='" + size + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }
}
