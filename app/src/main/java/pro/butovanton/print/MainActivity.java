package pro.butovanton.print;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements ItemClickListener {

    final int PICTURE_REQUEST_CODE = 101;

    private TextView textViewTel;
    private Pref pref;
    private List<Order> orders = new ArrayList<>();
    private Button buttonPrint;

    private Engine engine;
    private RecyclerView recyclerView;
    private RecyclerAdapterPrint adapterPrint;
    private LinearLayoutManager lm;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new Pref(getApplicationContext());

          recyclerView = findViewById(R.id.reciclerOrders);
        adapterPrint = new RecyclerAdapterPrint(this, getApplicationContext());
        lm = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager( lm );
        recyclerView.setAdapter(adapterPrint);

        addNewOrder();

        if (pref.getTel().equals("")) startActivity(new Intent(this, LoginActivity.class));
        textViewTel = findViewById(R.id.userTel);
        engine = new Engine(getApplication());

        buttonPrint = findViewById(R.id.buttonPrint);
        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               engine.uploadList(orders);
                addNewOrder();
            }
        });
    }

    private void addNewOrder() {
        Order order = new Order();
        order.tel = pref.getTel();
        orders.add(order);
        adapterPrint.adnotify(orders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case (R.id.ver_app):
                PackageInfo packageInfo = null;
                try {
                    packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String ver = packageInfo.versionName ;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Версия программы:");
                builder.setMessage(ver);
                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
        }



        return super.onOptionsItemSelected(item);
    }

        @Override
    protected void onResume() {
        super.onResume();
        textViewTel.setText(pref.getTel());
        buttonPrint.setVisibility(View.VISIBLE);
        for (Order order : orders)
            if (order.uri == null)
                buttonPrint.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mAuth.signOut();
    }

    @Override
    public void onItemClickChanger(Order order) {

    }

    @Override
    public void onItemClickDelete(int position) {

    }

    @Override
    public void onItemClickImage(int position) {
        this.position = position;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Выберите файл"), PICTURE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
        Order order = orders.get(position);
        order.uri = selectedImage;
        orders.set(position, order);
        adapterPrint.adnotify(orders);
            //  engine.uploadFileToStorage(selectedImage);
        onResume();
        }
    }
}