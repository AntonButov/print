package pro.butovanton.print;

public interface ItemClickListener {
        void onItemClickChanger(Order order);
        void onItemClickDelete(int position);
        void onItemClickImage(int position);
    }
