package pro.butovanton.print;

import java.util.ArrayList;
import java.util.List;

public class Size {

    private static final Size Instance = new Size();
    public List<Item> size = new ArrayList<>();

    private Size() {
        size.add(new Item("10 x 15 (a6)", (float) 100));
        size.add(new Item("13 x 18", (float) 150));
        size.add(new Item("15 x 21 (a5)", (float) 200));
        size.add(new Item("21 x 30 (a4)", (float) 250));
    }

    public static Size getSize() {
        return Instance;
    }

}