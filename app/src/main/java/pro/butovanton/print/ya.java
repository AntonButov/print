package pro.butovanton.print;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import pro.butovanton.print.yandex.Credentials;
import pro.butovanton.print.yandex.RestClient;
import pro.butovanton.print.yandex.exceptions.CancelledUploadingException;
import pro.butovanton.print.yandex.exceptions.http.HttpCodeException;
import pro.butovanton.print.yandex.json.Link;

public class ya {

    private static final String CREDENTIALS = "example.credentials";
    private static final String SERVER_PATH = "example.server.path";
    private static final String LOCAL_FILE = "example.local.file";

    private Credentials credentials;
    private String serverPath, localFile;

    public void uploadFile(final Credentials credentials, final String serverPath, final String localFile) {

        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    RestClient client = RestClientUtil.getInstance(credentials);
                    Link link = client.getUploadLink(serverPath, true);
                    client.uploadFile(link, true, new File(localFile), UploadFileRetainedFragment.this);
                    uploadComplete();
                } catch (CancelledUploadingException ex) {
                    // cancelled by user
                } catch (HttpCodeException ex) {
                    Log.d("DEBUG", "uploadFile", ex);
                    sendException(ex.getResponse().getDescription());
                } catch (IOException | ServerException ex) {
                    Log.d(TAG, "uploadFile", ex);
                    sendException(ex);
                }
            }
        }).start();
    }

}
