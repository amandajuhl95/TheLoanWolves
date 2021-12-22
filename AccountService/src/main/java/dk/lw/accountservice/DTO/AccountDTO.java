package dk.lw.accountservice.DTO;

import dk.lw.accountservice.domain.Account;
import dk.lw.accountservice.domain.AccountType;
import dk.lw.accountservice.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private UUID id;
    private UUID userId;
    private double balance;
    private AccountType type;

    private List<TransactionDTO> transactions = new ArrayList<>();

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.userId = account.getUserId();
        this.balance = account.getBalance();
        this.type = account.getType();

        for(Transaction transaction : account.getTransactions()) {
            transactions.add(new TransactionDTO(transaction));
        }
    }
}
