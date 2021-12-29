package dk.lw.loanwolvesservice.application;

import dk.lw.loanwolvesservice.AppSettings;
import dk.lw.loanwolvesservice.DTO.account.AccountDTO;
import dk.lw.loanwolvesservice.DTO.account.TransactionDTO;
import dk.lw.loanwolvesservice.DTO.loan.AmortizationDTO;
import dk.lw.loanwolvesservice.DTO.loan.LoanRequestDTO;
import dk.lw.loanwolvesservice.DTO.login.UserDTO;
import dk.lw.loanwolvesservice.Utils;
import dk.lw.loanwolvesservice.domain.LoanQuoteStatus;
import dk.lw.loanwolvesservice.domain.TransactionType;
import dk.lw.loanwolvesservice.errorhandling.LoanException;
import dk.lw.loanwolvesservice.errorhandling.UnauthorizedException;
import dk.lw.loanwolvesservice.infrastructure.AccountClient;
import dk.lw.loanwolvesservice.infrastructure.LoanClient;
import dk.lw.loanwolvesservice.infrastructure.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/loan-wolves")
public class LoanController {

    private LoanClient loanClient = new LoanClient();
    private AccountClient accountClient = new AccountClient();
    private LoginClient loginClient = new LoginClient();

    @Autowired
    private Logger producer;

    @PostMapping("/loan/decision/{status}/{userId}/{loanQuoteId}")
    public HttpStatus loanDecision (@PathVariable LoanQuoteStatus status, @PathVariable UUID userId, @PathVariable UUID loanQuoteId, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                HttpStatus statusCode = loanClient.loanDecision(userId, loanQuoteId, status);
                return statusCode;
            }
            String error = "Unauthorized for this action";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "Login expired";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
        throw new UnauthorizedException(error);
    }

    @PostMapping("/loan/amortization/{userId}/{accountId}/{loanId}")
    public HttpStatus loanAmortization (@PathVariable UUID userId, @PathVariable UUID loanId, @PathVariable UUID accountId, @RequestBody @Valid TransactionDTO transaction, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException, LoanException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                if(transaction.getType().equals(TransactionType.WITHDRAWAL)) {
                    AccountDTO accountDTO = accountClient.transaction(userId, accountId, transaction);
                    int transactionsSize = accountDTO.getTransactions().size();

                    AmortizationDTO amortizationDTO = new AmortizationDTO(accountDTO.getTransactions().get(transactionsSize - 1));
                    HttpStatus statusCode = loanClient.loanAmortization(loanId, amortizationDTO);
                    return statusCode;
                }
                String error = "Loan amortization must be a withdrawal";
                producer.sendLogs(AppSettings.serviceName, error, HttpStatus.BAD_REQUEST.value());
                throw new LoanException(error);
            }
            String error = "Unauthorized for this action";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "Login expired";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
        throw new UnauthorizedException(error);
    }

    @PostMapping("/loan/request/{userId}")
    public HttpStatus loanRequest (@PathVariable UUID userId, @RequestBody double amount, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException, LoanException {
        if(amount >= 5000) {
            if (Utils.validToken(token)) {
                if (Utils.authorize(token, userId)) {
                    UserDTO userDTO = loginClient.getUserById(userId);
                    LoanRequestDTO loanRequest = new LoanRequestDTO(amount, userDTO);
                    HttpStatus statusCode = loanClient.requestLoan(loanRequest);
                    return statusCode;
                }
                String error = "Unauthorized for this action";
                producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
                throw new UnauthorizedException(error);
            }
            String error = "Login expired";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "The minimum loan request is 5000 kr.";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.BAD_REQUEST.value());
        throw new LoanException(error);
    }
}
