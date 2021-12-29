package dk.lw.loanamortizationservice.DTO;

import dk.lw.loanamortizationservice.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAdmissionDTO {

    private UUID userId, loanQuoteId;
    private Status status;

}
