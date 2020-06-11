package pro.butovanton.print;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTel;
    private Pref pref;
    private Spinner spinner;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = new Pref(getApplicationContext());
        if (pref.getTel().equals("")) startActivity(new Intent(this, LoginActivity.class));
        textViewTel = findViewById(R.id.userTel);
        order = new Order();

    spinner  = findViewById(R.id.spinnerSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        order.tel = pref.getTel();
        textViewTel.setText(order.tel);
    }
}