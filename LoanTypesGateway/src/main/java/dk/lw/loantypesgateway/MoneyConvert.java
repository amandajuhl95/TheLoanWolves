package dk.lw.loantypesgateway;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MoneyConvert {

    public double convert() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=SEK&to=DKK&amount=1")
                .get()
                .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "a6bcbb2743msh98c0fb0dd9ffeacp1f9c4cjsnac688d386300")
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        JSONObject data = new JSONObject(jsonData);
        //System.out.println(Double.parseDouble(data.getJSONObject("rates").getJSONObject("DKK").getString("rate")));
        return Double.parseDouble(data.getJSONObject("rates").getJSONObject("DKK").getString("rate"));
    }
}
