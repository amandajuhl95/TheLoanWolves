package dk.lw.accountservice.application;

import dk.lw.accountservice.AppSettings;
import dk.lw.accountservice.DTO.AccountDTO;
import dk.lw.accountservice.DTO.TransactionDTO;
import dk.lw.accountservice.domain.Account;
import dk.lw.accountservice.domain.AccountType;
import dk.lw.accountservice.domain.Transaction;
import dk.lw.accountservice.errorhandling.InvalidTransactionException;
import dk.lw.accountservice.errorhandling.NotAllowedException;
import dk.lw.accountservice.errorhandling.NotFoundException;
import dk.lw.accountservice.infrastructure.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Logger producer;

    @PostMapping("/new/{type}/{userId}")
    public AccountDTO createAccount(@PathVariable UUID userId, @PathVariable AccountType type) {
        Account account = new Account(userId, type);
        account = accountRepository.save(account);
        return new AccountDTO(account);
    }

    @PostMapping("/{userId}/{accountId}")
    public AccountDTO transaction(@PathVariable UUID userId, @PathVariable UUID accountId, @RequestBody @Valid TransactionDTO transactionDTO) throws InvalidTransactionException, NotFoundException {
        Optional<Account> optAccount = accountRepository.findById(accountId);
        if(optAccount.isPresent())
        {
            Account account = optAccount.get();
            if(account.getUserId().equals(userId)) {
                Transaction transaction = account.addTransaction(transactionDTO);

                if(transaction != null) {
                    account = accountRepository.save(account);
                    return new AccountDTO(account);
                }
                String error = "The account balance is too low to perform transaction";
                producer.sendLogs(AppSettings.serviceName, error, HttpStatus.FORBIDDEN.value());
                throw new InvalidTransactionException(error);
            }
            String error = "User does not own this account";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.FORBIDDEN.value());
            throw new InvalidTransactionException(error);
        }
        String error = "Account: " + accountId + " not found";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.NOT_FOUND.value());
        throw new NotFoundException(error);
    }

    @GetMapping("/{userId}/{accountId}")
    public AccountDTO getAccount(@PathVariable @Valid UUID userId, @PathVariable @Valid UUID accountId) throws NotAllowedException, NotFoundException {
        Optional<Account> optAccount = accountRepository.findById(accountId);
        if(optAccount.isPresent())
        {
            Account account = optAccount.get();
            if(account.getUserId().equals(userId)) {
                return new AccountDTO(account);
            }
            String error = "User does not own this account";
            producer.sendLogs(AppSettings.serviceName, error, HttpStatus.FORBIDDEN.value());
            throw new NotAllowedException(error);
        }
        String error = "Account: " + accountId + " not found";
        producer.sendLogs(AppSettings.serviceName, error, HttpStatus.NOT_FOUND.value());
        throw new NotFoundException(error);
    }

    @GetMapping("/{userId}")
    public List<AccountDTO> getAccounts(@PathVariable @Valid UUID userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (Account account : accounts) {
            accountDTOs.add(new AccountDTO(account));
        }
        return accountDTOs;
    }
}
