package dk.lw.loanservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAdmissionDTO {

    private UUID userId, loanQuoteId;
    private LoanQuoteStatus status;

}