package dk.lw.loanquoteservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanQuoteDTO {

    private UUID id, userId;
    private String cpr;
    private double amount, fee, interestRate;
    private int duration;

}
