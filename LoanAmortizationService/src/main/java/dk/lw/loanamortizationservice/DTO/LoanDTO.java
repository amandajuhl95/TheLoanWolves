package dk.lw.loanamortizationservice.DTO;

import dk.lw.loanamortizationservice.domain.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private UUID userId;
    private double amount;

    public LoanDTO(Loan loan) {
        this.userId = loan.getUserId();
        this.amount = loan.getAmount();
    }

}
