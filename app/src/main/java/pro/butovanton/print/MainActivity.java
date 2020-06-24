package pro.butovanton.print;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements ItemClickListener {

    final int PICTURE_REQUEST_CODE = 101;

    private TextView textViewTel, textViewResult;
    private Pref pref;
    private List<Order> orders = new ArrayList<>();
    private Button buttonPrint;

    private Engine engine;
    private RecyclerView recyclerView;
    private RecyclerAdapterPrint adapterPrint;
    private LinearLayoutManager lm;
    private ProgressBar processBar;

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
        textViewResult = findViewById(R.id.textViewResult);
        engine = new Engine(getApplication());

        processBar = findViewById(R.id.progressBar);
        buttonPrint = findViewById(R.id.buttonPrint);
        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPrint.setVisibility(View.INVISIBLE);
                processBar.setMax(orders.size());
                processBar.setProgress(1);
                processBar.setVisibility(View.VISIBLE);
                engine.uploadList(orders).subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                        integer ++;
                        Log.d("DEBUG", "upload: " + integer + " files");
                        processBar.setProgress(integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        buttonPrint.setVisibility(View.VISIBLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Загрузка завершена.");
                        builder.setMessage("Загружено: " + (orders.size() - 1) + " файлов.");
                        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            processBar.setProgress(0);
                            }
                        });
                        builder.show();
                        System.out.println("uppload ok");
                    }
                });
            }
        });
    }

    private void addNewOrder() {
        Order order = new Order();
        order.tel = pref.getTel();
        order.num = orders.size();
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
        buttonPrint.setVisibility(View.INVISIBLE);
        for (Order order : orders) {
            if (order.uri != RecyclerAdapterPrint.uriDefault)
                buttonPrint.setVisibility(View.VISIBLE);
        }
        textViewResult.setText("Итого: " + oredersGetResult());
    }

    private String oredersGetResult() {
        float result = 0;
        for ( Order order : orders)
                result = result + order.getPrice();
    return String.valueOf(result);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mAuth.signOut();
    }

    @Override
    public void onItemClickChanger() {
        onResume();
    }

    @Override
    public void onItemClickDelete(int position) {
        orders.remove(position);
        adapterPrint.adnotify(orders);
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
       Uri lastUri = orders.get(orders.size()-1).uri;
       if (lastUri != RecyclerAdapterPrint.uriDefault)
           addNewOrder();
            //  engine.uploadFileToStorage(selectedImage);
        onResume();
        }
    }
}