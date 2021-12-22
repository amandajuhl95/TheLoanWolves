package dk.lw.loanamortizationservice.infrastructure;

import dk.lw.loanamortizationservice.domain.LoanQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LoanQuoteRepository extends JpaRepository<LoanQuote, UUID> {
}
