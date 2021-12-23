package dk.lw.loanwolvesservice.infrastructure;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class RestClient {
    final Gson gson = new Gson();
    String baseUrl;


    public Response POST(String url, String content) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(baseUrl+url)
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response GET(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(baseUrl+url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
