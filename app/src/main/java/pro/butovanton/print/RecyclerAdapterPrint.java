package pro.butovanton.print;

import android.content.Context;
import android.content.Intent;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class RecyclerAdapterPrint extends RecyclerView.Adapter<RecyclerAdapterPrint.ViewHolderPrint> {


    private List<Order> orders;
    private final LayoutInflater mInflater;
    private MainActivity mainActivity;
    private Context context;

    public RecyclerAdapterPrint(MainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        mInflater = LayoutInflater.from(context);
        orders = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderPrint onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        ViewHolderPrint vh = new ViewHolderPrint(view);
        return vh;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolderPrint holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapterPrint.ViewHolderPrint holder, final int positionAdapter) {

        Picasso
                .get()
                .load(orders
                .get(positionAdapter)
                .uri)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   mainActivity.onItemClick(listUsers.get(position).login);
            }
        });

        holder.spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orders.get(positionAdapter).quality = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.spinerQuality.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orders.get(positionAdapter).quality = parent.getItemAtPosition(position).toString();
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
        private Button buttonUpload;
        private Spinner spinnerSize, spinerQuality, spinnerQuantity;

        public ViewHolderPrint(View view) {
            super(view);
            imageView =  view.findViewById(R.id.imageView);

            spinnerSize = view.findViewById(R.id.spinnerSize);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.size_array, R.layout.spiner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSize.setAdapter(adapter);

              spinerQuality = view.findViewById(R.id.spinnerSizeQuality);
            ArrayAdapter<CharSequence> adapterQuality = ArrayAdapter.createFromResource(context,
                    R.array.quality_array, R.layout.spiner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinerQuality.setAdapter(adapterQuality);
        }

    }

}