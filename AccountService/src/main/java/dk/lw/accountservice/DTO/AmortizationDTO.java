package dk.lw.accountservice.DTO;

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
