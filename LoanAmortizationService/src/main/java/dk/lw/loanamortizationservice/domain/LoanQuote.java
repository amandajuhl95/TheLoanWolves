package dk.lw.loanamortizationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LoanQuote {

    @Id
    private UUID id;
    private UUID userId;
    private double amount, fee, interestRate;
    private int duration;

}
