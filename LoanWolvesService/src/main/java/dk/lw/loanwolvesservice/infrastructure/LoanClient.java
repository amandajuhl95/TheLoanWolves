package dk.lw.loanwolvesservice.infrastructure;

import dk.lw.loanwolvesservice.AppSettings;
import dk.lw.loanwolvesservice.DTO.AccountDTO;
import dk.lw.loanwolvesservice.DTO.LoanRequestDTO;
import dk.lw.loanwolvesservice.DTO.TransactionDTO;
import dk.lw.loanwolvesservice.domain.AccountType;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.UUID;

public class LoanClient extends RestClient {

    public LoanClient() {
        this.baseUrl = AppSettings.loanServiceURL;
    }

    public HttpStatus acceptLoan(UUID userId, UUID loanQuoteId) throws IOException {
        String url = "accept/"+ userId + "/" + loanQuoteId;
        int responseCode = POST(url, "").code();

        return HttpStatus.valueOf(responseCode);
    }

    public HttpStatus loanAmortization(UUID loanId, TransactionDTO transaction) throws IOException {
        String url = "amortization/"+ loanId;
        int responseCode = POST(url, gson.toJson(transaction)).code();

        return HttpStatus.valueOf(responseCode);
    }

    public HttpStatus requestLoan(LoanRequestDTO loanRequest) throws IOException {
        String url = "request";
        int responseCode = POST(url, gson.toJson(loanRequest)).code();

        return HttpStatus.valueOf(responseCode);
    }
}
