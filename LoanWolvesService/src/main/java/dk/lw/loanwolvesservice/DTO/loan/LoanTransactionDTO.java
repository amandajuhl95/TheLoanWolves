package dk.lw.loanwolvesservice.DTO.loan;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanTransactionDTO {
    private UUID transactionId;
    private TransactionType type;

    @DecimalMin(value = "1.00", message = "Amount should be minimum 1.00 kr.")
    private double amount;
}
