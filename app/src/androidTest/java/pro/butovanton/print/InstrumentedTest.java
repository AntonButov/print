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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
/*
    public NetworkService networkService;
    public JSONPlaceHolderApi jsonPlaceHolderApi;

  //  @Before
 //   public void init() {
  //      networkService = NetworkService.getInstance();

    @Test
    public  void diskTest() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(1);
        jsonPlaceHolderApi = networkService.getJSONApi();
        jsonPlaceHolderApi.disk().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    Log.d("DEBUG", "str = " + str);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                count.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            count.countDown();
            }
        });
        count.await();
    }

    @Test
    public void uploadTest() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(1);
        jsonPlaceHolderApi = networkService.getJSONApi();
        jsonPlaceHolderApi.upload().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    Log.d("DEBUG", "str = " + str);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                count.countDown();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                count.countDown();
            }
        });
        count.await();
    }

    @Test
    public void uploadImage(File file) {
        // create multipart
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
    }
 */

    @Test
    public void delPref() {
        Pref pref = new Pref(context);
        pref.saveTel("");
    }


}