package pro.butovanton.print;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface JSONPlaceHolderApi {
    @Headers("Authorization: OAuth AgAAAAA7QH8JAADLW3a7j_hCQ0JXnJH1QUJA-8w")
  //  @Headers("Authorization: OAuth AgAAAAA7QH8JAAZkOX0H1u-N4kpVhbnwjNK9JhM")
    @GET("disk/")
    Call<ResponseBody> disk();

    //  @Headers("Authorization: OAuth AgAAAAA7QH8JAAZkOX0H1u-N4kpVhbnwjNK9JhM")
    @GET("disk/resources/upload?path=test2")
    Call<ResponseBody> upload();

    @PUT("disk/resources/upload?path=test2")
    Call<ResponseBody> uploadFile(@Url String url, @Body RequestBody fileBody);

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );
}
