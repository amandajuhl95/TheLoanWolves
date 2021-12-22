package dk.lw.loanservice.application;

import dk.lw.loanservice.DTO.LoanRequestDTO;
import dk.lw.loanservice.DTO.TransactionDTO;
import dk.lw.loanservice.infrastructure.LoanQuoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/loan")
public class Controller {

    @Autowired
    private Producer producer;

    @PostMapping("/accept/{userId}/{loanQuoteId}")
    public void acceptLoan(@PathVariable UUID userId, @PathVariable UUID loanQuoteId) {
        producer.sendLoan(userId, loanQuoteId);
    }

    @PostMapping("/amortization/{loanId}")
    public void loanAmortization(@RequestBody TransactionDTO transactionDTO, @PathVariable UUID loanId) {
        producer.sendAmortization(transactionDTO, loanId);
    }

    @PostMapping("/request")
    public void requestLoan(@RequestBody LoanRequestDTO loanRequest) throws IOException {
        LoanQuoteClient.requestLoanQuote(loanRequest);
    }

}
