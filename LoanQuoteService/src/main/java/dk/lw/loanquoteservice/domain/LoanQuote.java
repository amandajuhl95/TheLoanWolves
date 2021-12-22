package dk.lw.loanquoteservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String cpr;
    private double amount, fee, interestRate;
    private int duration;

}
