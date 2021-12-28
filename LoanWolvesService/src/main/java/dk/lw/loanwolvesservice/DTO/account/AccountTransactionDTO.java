package dk.lw.loanwolvesservice.DTO.account;

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
public class AccountTransactionDTO {
    private UUID id;
    private double amount;
    private TransactionType type;
    private String date;
}
