package pro.butovanton.print;

import android.net.Uri;

import java.util.HashMap;

public class Order {
    public String tel;
    public int size;
    public int quality;
    public int quantity;
    public int num;
    public Uri uri;

    public HashMap<String, Integer> qualityPice = new HashMap();
    public HashMap<String, Integer> sizePrice = new HashMap();

    private void init() {
        tel = "77777777";
        size = 0;
        quality = 0;
        quantity = 1;
        num = 1;
    }

    public Order() {
       init();
       this.uri = RecyclerAdapterPrint.uriDefault;
    }

    public Order(Uri uri) {
      init();
      this.uri = uri;
        }

    public float getPrice() {
        float ret;
        ret = Size.getSize().size.get(size).getPrice() * Quality.getQuality().quality.get(quality).getPrice() * quantity;
        if (uri == RecyclerAdapterPrint.uriDefault) ret = 0;
        return ret;
    }

    @Override
    public String toString() {
        return "Order{" +
                "tel='" + tel + '\'' +
                ", size='" + Size.getSize().size.get(size).name + '\'' +
                ", quality='" + Quality.getQuality().quality.get(quality).name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", num=" + num +
                ", uri=" + uri +
                '}';
    }
}
