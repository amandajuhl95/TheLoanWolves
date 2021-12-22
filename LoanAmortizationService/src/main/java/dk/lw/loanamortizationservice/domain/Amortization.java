package dk.lw.loanamortizationservice.domain;

import dk.lw.loanamortizationservice.DTO.AmortizationDTO;
import dk.lw.loanamortizationservice.Utils;
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
        this.payment = amortizationDTO.getPayment();
        this.payOffAmount = calcActualPayment(amortizationDTO.getPayment());
        this.date = Utils.formatter.format(new Date());
    }

    private double calcActualPayment(double paid) {
        double paidInterest = this.loan.getInterestRate() / 100 * paid;
        return paid - paidInterest;
    }


}
