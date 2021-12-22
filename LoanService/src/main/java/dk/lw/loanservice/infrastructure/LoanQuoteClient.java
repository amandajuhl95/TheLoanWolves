package dk.lw.loanservice.infrastructure;

import dk.lw.loanservice.DTO.LoanRequestDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoanQuoteClient {

    public static void requestLoanQuote(LoanRequestDTO loanRequestDTO) throws IOException {

        try {
        String task = "636aa055-6348-11ec-a70d-0242ac110003";

        URL url = new URL("http://localhost:8080/engine-rest/process-definition/" + task + "/start");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"variables\":{" +
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
                "\"value\": \"" + loanRequestDTO.getUser().getAddress().getStreet() + " " + loanRequestDTO.getUser().getAddress().getNumber() + ", " + loanRequestDTO.getUser().getAddress().getZipcode() + " " + loanRequestDTO.getUser().getAddress().getCity() + "\" } } }";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } } catch(IOException e) {
            throw new IOException();
        }
    }
}
