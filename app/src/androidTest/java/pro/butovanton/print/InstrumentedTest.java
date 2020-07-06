package pro.butovanton.print;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("pro.butovanton.print", appContext.getPackageName());
    }

    public Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void disk() throws InterruptedException {
        NetworkService networkService;
        JSONPlaceHolderApi jsonPlaceHolderApi;
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
        CountDownLatch count = new CountDownLatch(1);
        jsonPlaceHolderApi.disk().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assertTrue(response.code() == 200);
                count.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        count.await(10000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void createDir() throws InterruptedException {
        NetworkService networkService;
        JSONPlaceHolderApi jsonPlaceHolderApi;
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
        CountDownLatch count = new CountDownLatch(1);
        jsonPlaceHolderApi.createPath("89281240876").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assertTrue(response.code() == 201 || response.code() == 409);
                count.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        count.await(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void uploadList() throws InterruptedException {

        List<Order> orders = new ArrayList<>();
        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/drawable/test");
        Order order = new Order();
        order.uri = uri;
        orders.add(order);
        uri = Uri.parse("android.resource://"+context.getPackageName()+"/drawable/test2");
        order = new Order();
        order.uri = uri;
        orders.add(order);

        CountDownLatch count = new CountDownLatch(1);

        Engine engine = new Engine(context);
        engine.uploadList(orders).subscribe(new DisposableObserver<Integer>() {
            @Override
            public void onNext(@NonNull Integer integer) {
                integer ++;
                Log.d("DEBUG", "upload: " + integer + " files");

            }

            @Override
            public void onError(@NonNull Throwable e) {
            assertTrue(false);
            }

            @Override
            public void onComplete() {
                System.out.println("test ok");
                count.countDown();
            }
        });

        count.await(1, TimeUnit.MINUTES);
    }

    @Test
    public void delPref() {
        Pref pref = new Pref(context);
        pref.saveTel("");
    }

}