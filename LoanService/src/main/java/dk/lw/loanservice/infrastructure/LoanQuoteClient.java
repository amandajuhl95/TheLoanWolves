package dk.lw.loanservice.infrastructure;

import dk.lw.loanservice.AppSettings;
import dk.lw.loanservice.DTO.LoanRequestDTO;
import dk.lw.loanservice.application.Logger;
import okhttp3.*;
import org.springframework.http.HttpStatus;


import java.io.IOException;

public class LoanQuoteClient {

    public static void requestLoanQuote(LoanRequestDTO loanRequestDTO) throws LoanQuoteClientException {
        try {
            String loanRequest = createRequest(loanRequestDTO);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,loanRequest);
            Request request = new Request.Builder()
                    .url(AppSettings.camundaURL)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            client.newCall(request).execute();

        } catch(IOException e) {
            String error = e.getMessage();

            Logger.sendLogs(AppSettings.serviceName, error, HttpStatus.FORBIDDEN.value());
            throw new LoanQuoteClientException(e);
        }
    }

    private static String createRequest(LoanRequestDTO loanRequestDTO) {

        String request = "{\"variables\":{" +
                "\"FullName\":{" +
                "\"value\": \"" + loanRequestDTO.getUser().getFullName() + "\"" +
                "}," +
                "\"Age\":{" +
                "\"value\":" + loanRequestDTO.getUser().getAge() +
                "}," +
                "\"Email\":{" +
                "\"value\": \"" + loanRequestDTO.getUser().getEmail() + "\"" +
                "}," +
                "\"UserId\":{" +
                "\"value\": \"" + loanRequestDTO.getUser().getId() + "\"" +
                "}," +
                "\"CPR\":{" +
                "\"value\": \"" + loanRequestDTO.getUser().getCpr() + "\"" +
                "}," +
                "\"Salary\":{" +
                "\"value\": " + loanRequestDTO.getUser().getSalary() +
                "}," +
                "\"Amount\":{" +
                "\"value\": " + loanRequestDTO.getAmount() +
                "}," +
                "\"Address\":{" +
                "\"value\": \"" + loanRequestDTO.getUser().getAddress().getStreet() + " " +
                loanRequestDTO.getUser().getAddress().getNumber() + ", " +
                loanRequestDTO.getUser().getAddress().getZipcode() + " " +
                loanRequestDTO.getUser().getAddress().getCity() + "\" } } }";

        return request;
    }
}
