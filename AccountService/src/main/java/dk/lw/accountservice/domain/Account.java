package dk.lw.accountservice.domain;

import dk.lw.accountservice.DTO.TransactionDTO;
import lombok.Data;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Account {
    @Id
    private UUID id;
    private UUID userId;
    private double balance;
    private AccountType type;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Transaction> transactions = new ArrayList<>();

    public Account(UUID userId, AccountType type) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.balance = 0;
        this.type = type;
    }

    public Account() {}

    public Transaction addTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = null;

        if(transactionDTO.getType().equals(TransactionType.WITHDRAWAL) && balance >= transactionDTO.getAmount()) {
            this.balance = balance - transactionDTO.getAmount();
            transaction = new Transaction(transactionDTO, this);
            this.transactions.add(transaction);

        } else if(transactionDTO.getType().equals(TransactionType.DEPOSIT)) {
            this.balance += transactionDTO.getAmount();
            transaction = new Transaction(transactionDTO, this);
            this.transactions.add(transaction);
        }
        return transaction;
    }
}
