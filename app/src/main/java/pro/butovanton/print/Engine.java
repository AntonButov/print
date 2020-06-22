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
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Engine {

    private NetworkService networkService;
    private JSONPlaceHolderApi jsonPlaceHolderApi;
    private Context contex;
    private File file;
    private String link;
    private StorageReference orderRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private Integer i = 0;
    private Boolean wait;
    private Uri uriConfigObser;
    private Order orderThis;


    public Engine(Context contex) {
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
        this.contex = contex;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
            mAuth.signInAnonymously();
    }


public Observable<Integer> uploadList(List<Order> orders) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                i = 0;
                for (Order order: orders) {
                    wait = true;
                    orderThis = order;
                    uploadFileToStorage(order.uri).subscribe(new SingleObserver<Uri>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Uri uri) {
                            uriConfigObser = uri;
                            wait = false;
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.d("DEBUG", "error " + e);
                        }
                    });
                    while (wait)
                    Thread.sleep(1000);
                wait = true;
                uploadFileToStorageConfig(uriConfigObser).subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                        wait = false;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        wait = false;
                        Log.d("DEBUG", "error " + e);
                    }
                });
                 while (wait)
                       Thread.sleep(1000);

                emitter.onNext(i ++);
                }
            emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
}

    private Single<Uri> uploadFileToStorage(Uri uri) {
       return Single.create(new SingleOnSubscribe<Uri>()  {
           @Override
           public void subscribe(@io.reactivex.rxjava3.annotations.NonNull SingleEmitter<Uri> emitter) throws Throwable {
               file = new File(uri.getPath());
               orderRef = mStorageRef.child(orderThis.tel).child(file.getName());
               orderRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       orderRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri url) {
                               Log.d("DEBUG", "Patch = " + url);
                               link = url.toString();
                               jsonPlaceHolderApi.createPath(orderThis.tel).enqueue(new Callback<ResponseBody>() {
                                   @Override
                                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                       jsonPlaceHolderApi.uploadFile("/" + orderThis.tel + "/" + "order_" + orderThis.num, link).enqueue(new Callback<ResponseBody>() {
                                           @Override
                                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                               writeToFile(orderThis.toString(), contex);
                                               File file = new File(contex.getFilesDir()+ "/config.txt");
                                               Uri uriConfig = Uri.fromFile(file);
                                               emitter.onSuccess(uriConfig);
                                           }

                                           @Override
                                           public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                emitter.onError(new Throwable("error order file"));
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
       });

    }

    private Single<Boolean>  uploadFileToStorageConfig(Uri uri) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull SingleEmitter<Boolean> emitter) throws Throwable {

                String filenameConfig = "config_" + orderThis.num + ".txt";
                StorageReference configRef = mStorageRef.child(orderThis.tel + "/" + filenameConfig);
                configRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        configRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri url) {
                                Log.d("DEBUG", "Patch = " + url);
                                link = url.toString();
                                jsonPlaceHolderApi.createPath(orderThis.tel).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        jsonPlaceHolderApi.uploadFile("/" + orderThis.tel + "/" + filenameConfig, link).enqueue(new Callback<ResponseBody>() {
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
                                               emitter.onSuccess(true);
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                emitter.onError(new Throwable("error config file"));
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
