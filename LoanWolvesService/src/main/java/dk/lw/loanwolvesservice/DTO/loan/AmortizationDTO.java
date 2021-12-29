package dk.lw.loanwolvesservice.DTO.loan;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.DTO.account.AccountTransactionDTO;
import dk.lw.loanwolvesservice.DTO.account.TransactionDTO;
import dk.lw.loanwolvesservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmortizationDTO {
    private UUID transactionId;
    private TransactionType type;

    @DecimalMin(value = "1.00", message = "Amount should be minimum 1.00 kr.")
    @Positive( message = "Amount should be greater than 0 kr.")
    private double amount;

    public AmortizationDTO(AccountTransactionDTO transaction) {
        this.transactionId = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
    }
}
