package dk.lw.loanamortizationservice.DTO;

import dk.lw.loanamortizationservice.domain.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmortizationDTO {

    private UUID loanId, transactionId;
    private double payment;

}
