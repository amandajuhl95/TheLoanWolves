package dk.lw.loanamortizationservice.infrastructure;

import dk.lw.loanamortizationservice.domain.Amortization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AmortizationRepository extends JpaRepository<Amortization, UUID> {
}
