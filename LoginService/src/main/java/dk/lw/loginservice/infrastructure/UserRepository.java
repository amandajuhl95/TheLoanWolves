package dk.lw.loginservice.infrastructure;

import dk.lw.loginservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findOneByCpr (@Param("cpr") String cpr);
}
