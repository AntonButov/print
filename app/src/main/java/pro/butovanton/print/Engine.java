package pro.butovanton.print;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class Engine {

    private NetworkService networkService;
    private JSONPlaceHolderApi jsonPlaceHolderApi;
    private Context contex;
    private Order order;
    private File file;
    private String link;
    private StorageReference orderRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    public Engine(Context contex, Order order) {
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
        this.contex = contex;
        this.order = order;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
            mAuth.signInAnonymously();
    }

   // public

    public void  uploadFileToStorage(Uri uri) {
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
                                        writeToFile(order.toString(), contex);
                                        File file = new File(contex.getFilesDir()+ "/config.txt");
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
                                        Toast toast = Toast.makeText(contex,"Файл доставлен.",Toast.LENGTH_SHORT);
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

}
