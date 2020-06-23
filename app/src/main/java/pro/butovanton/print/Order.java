package pro.butovanton.print;

import android.net.Uri;

public class Order {
    public String tel;
    public String size;
    public String quality;
    public String quantity;
    public int num;
    public Uri uri;

    public Order() {
        tel = "77777777";
        size = "15 x 20";
        quality = "econom";
        num = 1;
    }

    @Override
    public String toString() {
        return "Order{" +
                "tel='" + tel + '\'' +
                ", size='" + size + '\'' +
                ", quality='" + quality + '\'' +
                ", quantity='" + quantity + '\'' +
                ", num=" + num +
                ", uri=" + uri +
                '}';
    }
}
