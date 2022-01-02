package dk.lw.accountservice.infrastructure;

import dk.lw.accountservice.domain.Account;
import dk.lw.accountservice.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByUserIdAndType(UUID id, AccountType type);
    List<Account> findByUserId(UUID userId);
}
