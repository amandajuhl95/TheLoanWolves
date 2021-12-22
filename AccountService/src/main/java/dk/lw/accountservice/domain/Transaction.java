package dk.lw.accountservice.domain;

import dk.lw.accountservice.DTO.TransactionDTO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
public class Transaction {
    @Id
    private UUID id;
    private double amount;
    private TransactionType type;

    @ManyToOne
    private Account account;

    public Transaction(TransactionDTO transactionDTO, Account account) {
        this.id = UUID.randomUUID();
        this.amount = transactionDTO.getAmount();
        this.type = transactionDTO.getType();
        this.account = account;
    }

    public Transaction() {

    }
}
