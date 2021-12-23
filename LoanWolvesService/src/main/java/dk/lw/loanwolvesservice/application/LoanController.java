package dk.lw.loanwolvesservice.application;

import dk.lw.loanwolvesservice.DTO.LoanRequestDTO;
import dk.lw.loanwolvesservice.DTO.TransactionDTO;
import dk.lw.loanwolvesservice.Utils;
import dk.lw.loanwolvesservice.errorhandling.UnauthorizedException;
import dk.lw.loanwolvesservice.infrastructure.AccountClient;
import dk.lw.loanwolvesservice.infrastructure.LoanClient;
import dk.lw.loanwolvesservice.infrastructure.LoginClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/loan-wolves")
public class LoanController {

    private LoanClient loanClient = new LoanClient();
    private AccountClient accountClient = new AccountClient();
    private LoginClient loginClient = new LoginClient();

    @PostMapping("/loan/accept/{userId}/{loanQuoteId}")
    public HttpStatus acceptLoan (@PathVariable UUID userId, @PathVariable UUID loanQuoteId, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                HttpStatus statusCode = loanClient.acceptLoan(userId, loanQuoteId);
                return statusCode;
            }
            throw new UnauthorizedException("Unauthorized for this action");
        }
        throw new UnauthorizedException("Login expired");
    }

    @PostMapping("/loan/amortization/{loanId}")
    public HttpStatus loanAmortization (@PathVariable UUID loanId, @RequestBody TransactionDTO transaction, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            HttpStatus statusCode = loanClient.loanAmortization(loanId, transaction);
            return statusCode;
        }
        throw new UnauthorizedException("Login expired");
    }

    @PostMapping("/loan/request")
    public HttpStatus loanRequest (@RequestBody LoanRequestDTO loanRequest, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, loanRequest.getUser().getId()))
            {
                HttpStatus statusCode = loanClient.requestLoan(loanRequest);
                return statusCode;
            }
            throw new UnauthorizedException("Unauthorized for this action");
        }
        throw new UnauthorizedException("Login expired");
    }
}
