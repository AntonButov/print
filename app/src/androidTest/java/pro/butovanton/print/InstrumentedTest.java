package pro.butovanton.print;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    public void uploadFile() throws InterruptedException {
        NetworkService networkService;
        JSONPlaceHolderApi jsonPlaceHolderApi;
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
        CountDownLatch count = new CountDownLatch(1);
        String link = "https://firebasestorage.googleapis.com/v0/b/print-a8bb9.appspot.com/o/%2B3755646782%2Fimage%3A105?alt=media&token=86e0030a-1a87-4210-a6c8-bace1c119098";
        jsonPlaceHolderApi.uploadFile("/89281240876/testww.jpg", link).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assertTrue(response.code() == 202);
                count.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        count.await(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void delPref() {
        Pref pref = new Pref(context);
        pref.saveTel("");
    }


}