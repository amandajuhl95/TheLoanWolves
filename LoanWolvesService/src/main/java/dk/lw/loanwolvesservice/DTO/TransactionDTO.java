package dk.lw.loanwolvesservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
    private UUID transactionId;
    private double amount;
    private TransactionType type;

    public TransactionDTO(double amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
    }
}
