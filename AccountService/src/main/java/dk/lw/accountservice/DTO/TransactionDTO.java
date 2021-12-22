package dk.lw.accountservice.DTO;

import dk.lw.accountservice.domain.Transaction;
import dk.lw.accountservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private double amount;
    private TransactionType type;

    public TransactionDTO(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
    }
}
