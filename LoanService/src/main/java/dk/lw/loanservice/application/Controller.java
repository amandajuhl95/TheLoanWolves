package dk.lw.loanservice.application;

import dk.lw.loanservice.DTO.LoanQuoteStatus;
import dk.lw.loanservice.DTO.LoanRequestDTO;
import dk.lw.loanservice.DTO.TransactionDTO;
import dk.lw.loanservice.infrastructure.LoanQuoteClient;
import dk.lw.loanservice.infrastructure.LoanQuoteClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/loan")
public class Controller {

    @Autowired
    private Producer producer;

    @PostMapping("/decision/{status}/{userId}/{loanQuoteId}")
    public void loanDecision(@PathVariable LoanQuoteStatus status, @PathVariable UUID userId, @PathVariable UUID loanQuoteId) {
        producer.sendLoan(status,userId, loanQuoteId);
    }

    @PostMapping("/amortization/{loanId}")
    public void loanAmortization(@RequestBody TransactionDTO transactionDTO, @PathVariable UUID loanId) {
        producer.sendAmortization(transactionDTO, loanId);
    }

    @PostMapping("/request")
    public void requestLoan(@RequestBody LoanRequestDTO loanRequest) throws LoanQuoteClientException {
        LoanQuoteClient.requestLoanQuote(loanRequest);
    }

}
