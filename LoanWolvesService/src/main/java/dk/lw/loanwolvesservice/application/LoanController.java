package dk.lw.loanwolvesservice.application;

import dk.lw.loanwolvesservice.AppSettings;
import dk.lw.loanwolvesservice.DTO.loan.LoanRequestDTO;
import dk.lw.loanwolvesservice.DTO.loan.LoanTransactionDTO;
import dk.lw.loanwolvesservice.DTO.login.UserDTO;
import dk.lw.loanwolvesservice.Utils;
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
    private LoggingProducer producer;

    @PostMapping("/loan/accept/{userId}/{loanQuoteId}")
    public HttpStatus acceptLoan (@PathVariable UUID userId, @PathVariable UUID loanQuoteId, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                HttpStatus statusCode = loanClient.acceptLoan(userId, loanQuoteId);
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

    @PostMapping("/loan/amortization/{loanId}")
    public HttpStatus loanAmortization (@PathVariable UUID loanId, @RequestBody @Valid LoanTransactionDTO transaction, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            HttpStatus statusCode = loanClient.loanAmortization(loanId, transaction);
            return statusCode;
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
