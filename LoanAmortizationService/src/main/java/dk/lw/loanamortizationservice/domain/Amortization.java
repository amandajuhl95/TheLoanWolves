package dk.lw.loanamortizationservice.domain;

import dk.lw.loanamortizationservice.AppSettings;
import dk.lw.loanamortizationservice.DTO.AmortizationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Amortization {

    @Id
    private UUID id;
    private UUID transactionId;
    private double payment, payOffAmount;
    private String date;

    @ManyToOne
    private Loan loan;

    public Amortization(AmortizationDTO amortizationDTO, Loan loan) {
        this.id = UUID.randomUUID();
        this.transactionId = amortizationDTO.getTransactionId();
        this.loan = loan;
        this.payment = amortizationDTO.getAmount();
        this.payOffAmount = calcActualPayment(amortizationDTO.getAmount());
        this.date = AppSettings.formatter.format(new Date());
    }

    private double calcActualPayment(double paid) {
        double paidInterest = this.loan.getInterestRate() / 100 * paid;
        return paid - paidInterest;
    }


}
