package dk.lw.loanamortizationservice.infrastructure;

import dk.lw.loanamortizationservice.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

}
