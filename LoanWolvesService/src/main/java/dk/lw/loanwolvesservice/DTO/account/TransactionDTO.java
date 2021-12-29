package dk.lw.loanwolvesservice.DTO.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.lw.loanwolvesservice.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    @DecimalMin(value = "1.00", message = "Amount should be minimum 1.00 kr.")
    @Positive( message = "Amount should be greater than 0 kr.")
    private BigDecimal amount;
    private TransactionType type;
}
