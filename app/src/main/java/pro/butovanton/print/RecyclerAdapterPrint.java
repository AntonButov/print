package pro.butovanton.print;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class RecyclerAdapterPrint extends RecyclerView.Adapter<RecyclerAdapterPrint.ViewHolderPrint> {

    static Uri uriDefault;
    private List<Order> orders;
    private final LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;

    public RecyclerAdapterPrint(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        mInflater = LayoutInflater.from(context);
        orders = new ArrayList<>();
        this.context = context;
        uriDefault = Uri.parse("android.resource://"+context.getPackageName()+"/drawable/test");
    }

    @NonNull
    @Override
    public ViewHolderPrint onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        ViewHolderPrint vh = new ViewHolderPrint(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapterPrint.ViewHolderPrint holder, final int positionAdapter) {
    Uri uri = orders.get(positionAdapter).uri;
        Picasso
                .get()
                .load(uri)
                .into(holder.imageView);
        holder.textViewItemPrice.setText(orders.get(positionAdapter).getPrice() + "Р");
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onItemClickImage(positionAdapter);
                mainActivity.onItemClickChanger();
            }
        });

        if (orders.get(positionAdapter).uri ==  uriDefault)
            holder.buttonDel.setVisibility(View.INVISIBLE);
        else {
            holder.buttonDel.setVisibility(View.VISIBLE);
            holder.buttonDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.onItemClickDelete(positionAdapter);
                    holder.textViewItemPrice.setText(orders.get(positionAdapter).getPrice() + "Р");
                }
            });
        }

        holder.spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orders.get(positionAdapter).quantity = position + 1;
                holder.textViewItemPrice.setText(orders.get(positionAdapter).getPrice() + "Р");
                mainActivity.onItemClickChanger();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        holder.spinerQuality.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orders.get(positionAdapter).quality = position;
                holder.textViewItemPrice.setText(orders.get(positionAdapter).getPrice() + "Р");
                mainActivity.onItemClickChanger();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinnerQuantity.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orders.get(positionAdapter).quantity = (position + 1);
                holder.textViewItemPrice.setText(orders.get(positionAdapter).getPrice() + "Р");
                mainActivity.onItemClickChanger();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void adnotify(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public class ViewHolderPrint extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private Spinner spinnerSize, spinerQuality, spinnerQuantity;
        private TextView textViewItemPrice;
        private Button buttonDel;

        public ViewHolderPrint(View view) {
            super(view);
            buttonDel = view.findViewById(R.id.buttonDel);
            imageView =  view.findViewById(R.id.imageView);
            textViewItemPrice = view.findViewById(R.id.textItemPrice);

            spinnerSize = view.findViewById(R.id.spinnerSize);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.size_array, R.layout.spinersize);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSize.setAdapter(adapter);

              spinerQuality = view.findViewById(R.id.spinnerQuality);
            ArrayAdapter<CharSequence> adapterQuality = ArrayAdapter.createFromResource(context,
                    R.array.quality_array, R.layout.spinerquality);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinerQuality.setAdapter(adapterQuality);

            spinnerQuantity = view.findViewById(R.id.spinnerQuantity);
            ArrayAdapter<CharSequence> adapterQuant = ArrayAdapter.createFromResource(context,
                    R.array.quantity_array, R.layout.spinerquantity);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerQuantity.setAdapter(adapterQuant);
        }

    }

}
