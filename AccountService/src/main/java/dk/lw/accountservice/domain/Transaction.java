package dk.lw.accountservice.domain;

import dk.lw.accountservice.DTO.AmortizationDTO;
import dk.lw.accountservice.DTO.TransactionDTO;
import dk.lw.accountservice.Utils;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Transaction {
    @Id
    private UUID id;
    private double amount;
    private TransactionType type;
    private TransactionStatus status;
    private String date;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Account account;

    public Transaction(TransactionDTO transactionDTO, Account account) {
        this.id = UUID.randomUUID();
        this.amount = transactionDTO.getAmount();
        this.type = transactionDTO.getType();
        this.status = TransactionStatus.COMPLETED;
        this.account = account;
        this.date = Utils.formatter.format(new Date());
    }

    public Transaction() {}

    public void cancelTransaction(AmortizationDTO amortizationDTO) {
        if(type.equals(TransactionType.WITHDRAWAL)
                && amount == amortizationDTO.getPayment()) {
            account.deposit(amount);
            status = TransactionStatus.CANCELED;
        }
    }
}
