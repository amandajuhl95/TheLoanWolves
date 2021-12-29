package dk.lw.accountservice.application;

import com.google.gson.Gson;
import dk.lw.accountservice.DTO.AmortizationDTO;
import dk.lw.accountservice.DTO.LoanDTO;
import dk.lw.accountservice.DTO.TransactionDTO;
import dk.lw.accountservice.domain.Account;
import dk.lw.accountservice.domain.AccountType;
import dk.lw.accountservice.domain.Transaction;
import dk.lw.accountservice.domain.TransactionType;
import dk.lw.accountservice.infrastructure.AccountRepository;
import dk.lw.accountservice.infrastructure.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanConsumer {
    private final Gson gson = new Gson();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /*@KafkaListener(topics = "loan-transfer", groupId = "loan")
    public void transferLoan(String request)
    {
        LoanDTO loanDTO = gson.fromJson(request, LoanDTO.class);
        Account account = accountRepository.findByIdAndType(loanDTO.getUserId(), AccountType.DEBIT);

        if(account == null)
        {
            account = accountRepository.save(new Account(loanDTO.getUserId(), AccountType.DEBIT));
        }

        account.addTransaction(new TransactionDTO(loanDTO.getAmount(), TransactionType.DEPOSIT));
        accountRepository.save(account);
    }

    @KafkaListener(topics = "denied-transfer", groupId = "loan")
    public void transferDenied(String request)
    {
        AmortizationDTO amortizationDTO = gson.fromJson(request, AmortizationDTO.class);
        Optional<Transaction> optTransaction = transactionRepository.findById(amortizationDTO.getTransactionId());

        if(optTransaction.isPresent()) {
            Transaction transaction = optTransaction.get();
            transaction.cancelTransaction(amortizationDTO);
            transactionRepository.save(transaction);
        }
    }*/
}
