package dk.lw.loanquoteservice.domain;

import dk.lw.loanquoteservice.DTO.LoanQuoteDTO;
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

    public LoanQuote(LoanQuoteDTO loanQuoteDTO) {
        this.id = loanQuoteDTO.getId();
        this.userId = loanQuoteDTO.getUserId();
        this.cpr = loanQuoteDTO.getCpr();
        this.amount = loanQuoteDTO.getAmount();
        this.fee = loanQuoteDTO.getFee();
        this.interestRate = loanQuoteDTO.getInterestRate();
        this.duration = loanQuoteDTO.getDuration();
    }
}
