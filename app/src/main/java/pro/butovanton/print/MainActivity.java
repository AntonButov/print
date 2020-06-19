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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {

    final int PICTURE_REQUEST_CODE = 101;

    private TextView textViewTel;
    private Pref pref;
    private Spinner spinnerSize, spinerQuality;
    private Order order;
    private StorageReference mStorageRef;
    private Button buttonUpload;
    private FirebaseAuth mAuth;
    private NetworkService networkService;
    private JSONPlaceHolderApi jsonPlaceHolderApi;
    private File file;
    private String link;
    private StorageReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
        pref = new Pref(getApplicationContext());
        if (pref.getTel().equals("")) startActivity(new Intent(this, LoginActivity.class));
        textViewTel = findViewById(R.id.userTel);
        order = new Order();

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

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
            mAuth.signInAnonymously();
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
            uploadFileToStorage(selectedImage);
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

    private void  uploadFileToStorage(Uri uri) {
       file = new File(uri.getPath());
       orderRef = mStorageRef.child(order.tel).child(file.getName());
       orderRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               orderRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri url) {
                       Log.d("DEBUG", "Patch = " + url);
                       link = url.toString();
                       jsonPlaceHolderApi.createPath(order.tel).enqueue(new Callback<ResponseBody>() {
                           @Override
                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               jsonPlaceHolderApi.uploadFile("/" + order.tel + "/" + "order_" + order.num, link).enqueue(new Callback<ResponseBody>() {
                                   @Override
                                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                       writeToFile(order.toString(), getBaseContext());
                                       File file = new File(getApplication().getFilesDir()+ "/config.txt");
                                       Uri uriConfig = Uri.fromFile(file);
                                       uploadFileToStorageConfig(uriConfig);
                                   }

                                   @Override
                                   public void onFailure(Call<ResponseBody> call, Throwable t) {

                                   }
                               });
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {

                           }
                       });
                   }
               });
           }
       });

    }

    private void  uploadFileToStorageConfig(Uri uri) {
            String filenameConfig = "config_" + order.num + ".txt";
        StorageReference configRef = mStorageRef.child(order.tel + "/" + filenameConfig);
        configRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                configRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri url) {
                        Log.d("DEBUG", "Patch = " + url);
                        link = url.toString();
                        jsonPlaceHolderApi.createPath(order.tel).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                jsonPlaceHolderApi.uploadFile("/" + order.tel + "/" + filenameConfig, link).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast toast = Toast.makeText(getApplicationContext(),"Файл доставлен.",Toast.LENGTH_SHORT);
                                        toast.show();
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                                configRef.delete();
                                                orderRef.delete();
                                        //           StorageReference del = mStorageRef.child(order.tel);
                                        //                  del.delete();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });

            }
        });
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }
}