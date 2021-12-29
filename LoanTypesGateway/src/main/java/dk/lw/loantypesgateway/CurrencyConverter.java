package dk.lw.loantypesgateway;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CurrencyConverter {

    public double convert() throws IOException {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(AppSettings.currencyConverterURL)
                    .get()
                    .addHeader("x-rapidapi-host", AppSettings.currencyConverterHost)
                    .addHeader("x-rapidapi-key", AppSettings.currencyConverterKey)
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject data = new JSONObject(jsonData);
            return Double.parseDouble(data.getJSONObject("rates").getJSONObject("DKK").getString("rate"));

        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
