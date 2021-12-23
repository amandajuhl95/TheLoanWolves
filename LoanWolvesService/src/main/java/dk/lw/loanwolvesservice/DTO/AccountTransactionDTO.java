package dk.lw.loanwolvesservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransactionDTO {
    private double amount;
    private TransactionType type;
    private String date;

}
