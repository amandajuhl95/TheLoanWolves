package dk.lw.loantypesgateway;

import dk.lw.loantypesgateway.logging.LoggingProducer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class CurrencyConverter {

    LoggingProducer producer = new LoggingProducer();

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
            producer.sendLogs(AppSettings.serviceName, e.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
            throw new IOException(e);
        }
    }
}
