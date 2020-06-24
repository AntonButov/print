package pro.butovanton.print;

import java.util.ArrayList;
import java.util.List;

public class Quality {

    private static final Quality Instance = new Quality();
    public List<Item> quality = new ArrayList<>();

    private Quality() {
        quality.add(new Item("Эконом", (float) 100));
        quality.add(new Item("Стандарт", (float) 150));
    }

    public static Quality getQuality() {
        return Instance;
    }
}
