package dk.lw.loanamortizationservice.domain;

import dk.lw.loanamortizationservice.AppSettings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    private UUID id;
    private UUID userId;
    private String startDate, endDate;
    private double amount, principal, interestRate;

    @OneToMany(mappedBy = "loan", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.EAGER)
    private List<Amortization> amortizations = new ArrayList<>();

    public Loan(LoanQuote loanQuote) {
        this.id = UUID.randomUUID();
        this.userId = loanQuote.getUserId();
        Date date = new Date();
        this.amount = loanQuote.getAmount();
        this.interestRate = loanQuote.getInterestRate();
        this.principal = loanQuote.getAmount() + loanQuote.getFee();
        this.startDate = AppSettings.formatter.format(date);
        this.endDate = AppSettings.formatter.format(DateUtils.addYears(date, loanQuote.getDuration()));
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", userId=" + userId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", amount=" + amount +
                ", principal=" + principal +
                ", interestRate=" + interestRate +
                '}';
    }

    public void addAmortization(Amortization amortization) {
        this.principal = principal - amortization.getPayOffAmount();
        this.amortizations.add(amortization);
    }


}
