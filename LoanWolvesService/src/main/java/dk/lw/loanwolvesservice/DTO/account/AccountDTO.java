package dk.lw.loanwolvesservice.DTO.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    private UUID id;
    private UUID userId;
    private double balance;
    private AccountType type;
    private List<AccountTransactionDTO> transactions;
}
