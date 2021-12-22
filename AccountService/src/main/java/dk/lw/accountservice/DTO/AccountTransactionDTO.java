package dk.lw.accountservice.DTO;

import dk.lw.accountservice.domain.Transaction;
import dk.lw.accountservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransactionDTO {
    private double amount;
    private TransactionType type;
    private String date;

    public AccountTransactionDTO(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.date = transaction.getDate();
    }
}
