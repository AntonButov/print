package pro.butovanton.print;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


public class MainActivity extends AppCompatActivity  {

    final int PICTURE_REQUEST_CODE = 101;

    private TextView textViewTel;
    private Pref pref;
    private Spinner spinnerSize, spinerQuality;
    private Order order;
    private Button buttonUpload;
    private Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new Pref(getApplicationContext());
        if (pref.getTel().equals("")) startActivity(new Intent(this, LoginActivity.class));
        textViewTel = findViewById(R.id.userTel);
        order = new Order();
        engine = new Engine(getApplication());

        spinnerSize = findViewById(R.id.spinnerSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.size_array, R.layout.spiner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);
        spinnerSize.setOnItemSelectedListener(itemSelectedListenerSize);

        spinerQuality = findViewById(R.id.spinnerSizeQuality);
        ArrayAdapter<CharSequence> adapterQuality = ArrayAdapter.createFromResource(this,
                R.array.quality_array, R.layout.spiner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerQuality.setAdapter(adapterQuality);
        spinerQuality.setOnItemSelectedListener(itemSelectedListenerQuality);

        buttonUpload = findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Выберите файл"), PICTURE_REQUEST_CODE);
            }
        });

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
          //  engine.uploadFileToStorage(selectedImage);
        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mAuth.signOut();
    }
}