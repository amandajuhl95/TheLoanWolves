package dk.lw.accountservice.application;

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
    private LoggingProducer producer;

    @PostMapping("/new/{type}/{userId}")
    public AccountDTO createAccount(@PathVariable @Valid UUID userId, @PathVariable @Valid AccountType type) {
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
                producer.sendLogs("AccountService", error, 400);
                throw new InvalidTransactionException(error);
            }
            String error = "User does not own this account";
            producer.sendLogs("AccountService", error, 400);
            throw new InvalidTransactionException(error);
        }
        String error = "Account: " + accountId + " not found";
        producer.sendLogs("AccountService", error, 404);
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
            throw new NotAllowedException("User does not own this account");
        }
        throw new NotFoundException("Account: " + accountId + " not found");
    }

    @GetMapping("/{userId}")
    public List<AccountDTO> getAccounts(@PathVariable @Valid UUID userId) throws NotAllowedException, NotFoundException {
        List<Account> accounts = accountRepository.findByUserId(userId);
        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (Account account : accounts) {
            accountDTOs.add(new AccountDTO(account));
        }
        return accountDTOs;
    }
}
