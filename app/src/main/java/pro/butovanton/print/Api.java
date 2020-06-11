package pro.butovanton.print;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Api {

    private Context context;
    private NetworkService networkService;
    private JSONPlaceHolderApi jsonPlaceHolderApi;
    private MutableLiveData<Response> diskMutable = new MutableLiveData<>();

   public Api(Context context) {
       this.context = context;
        networkService = NetworkService.getInstance();
        jsonPlaceHolderApi = networkService.getJSONApi();
    }

  public LiveData<Response> disk() {
       jsonPlaceHolderApi.disk().enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               diskMutable.setValue(response);
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

           }
       });
       return diskMutable;
  }


}
