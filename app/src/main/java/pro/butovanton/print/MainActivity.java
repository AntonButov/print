package pro.butovanton.print;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    private TextView textViewTel;
    private Pref pref;
    private Spinner spinner, spinerQuality;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = new Pref(getApplicationContext());
        if (pref.getTel().equals("")) startActivity(new Intent(this, LoginActivity.class));
        textViewTel = findViewById(R.id.userTel);
        order = new Order();

        spinner = findViewById(R.id.spinnerSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(itemSelectedListenerSize);

        spinerQuality = findViewById(R.id.spinnerSizeQuality);
        ArrayAdapter<CharSequence> adapterQuality = ArrayAdapter.createFromResource(this,
                R.array.quality_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerQuality.setAdapter(adapterQuality);
        spinerQuality.setOnItemSelectedListener(itemSelectedListenerQuality);
    }

    AdapterView.OnItemSelectedListener itemSelectedListenerQuality = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            order.quality = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener itemSelectedListenerSize = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            order.size = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

        @Override
    protected void onResume() {
        super.onResume();
        order.tel = pref.getTel();
        textViewTel.setText(order.tel);
    }

}