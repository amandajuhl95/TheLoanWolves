package dk.lw.loanwolvesservice.application;

import dk.lw.loanwolvesservice.AppSettings;
import dk.lw.loanwolvesservice.DTO.account.AccountDTO;
import dk.lw.loanwolvesservice.DTO.account.TransactionDTO;
import dk.lw.loanwolvesservice.Utils;
import dk.lw.loanwolvesservice.domain.AccountType;
import dk.lw.loanwolvesservice.errorhandling.UnauthorizedException;
import dk.lw.loanwolvesservice.infrastructure.AccountClient;
import dk.lw.loanwolvesservice.infrastructure.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loan-wolves")
public class AccountController {

    private AccountClient accountClient = new AccountClient();
    private LoginClient loginClient = new LoginClient();

    @Autowired
    private Logger producer;

    @GetMapping("/account/{userId}/{accountId}")
    public AccountDTO getAccount (@PathVariable UUID userId, @PathVariable UUID accountId, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {

        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                AccountDTO accountDTO = accountClient.getAccount(userId,accountId);
                return accountDTO;
            }
            String error = "Unauthorized for this action";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "Login expired";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
        throw new UnauthorizedException(error);
    }

    @GetMapping("/account/{userId}")
    public List<AccountDTO> getAccounts (@PathVariable UUID userId, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {

        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                List<AccountDTO> accountDTOs = accountClient.getAccounts(userId);
                return accountDTOs;
            }
            String error = "Unauthorized for this action";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "Login expired";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
        throw new UnauthorizedException(error);
    }

    @PostMapping("/account/transaction/{userId}/{accountId}")
    public AccountDTO transaction (@PathVariable UUID userId, @PathVariable UUID accountId, @RequestBody @Valid TransactionDTO transaction, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                AccountDTO accountDTO = accountClient.transaction(userId, accountId, transaction);
                return accountDTO;
            }
            String error = "Unauthorized for this action";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "Login expired";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
        throw new UnauthorizedException(error);
    }

    @PostMapping("/new/account/{type}/{userId}")
    public AccountDTO createAccount (@PathVariable AccountType type, @PathVariable UUID userId, @RequestHeader("Session-Token") String token) throws UnauthorizedException, IOException {
        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userId))
            {
                AccountDTO accountDTO = accountClient.createAccount(type, userId);
                return accountDTO;
            }
            String error = "Unauthorized for this action";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException(error);
        }
        String error = "Login expired";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.UNAUTHORIZED.value());
        throw new UnauthorizedException(error);
    }
}
