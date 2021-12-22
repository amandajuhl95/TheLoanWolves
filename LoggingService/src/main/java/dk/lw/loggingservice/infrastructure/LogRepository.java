package dk.lw.loggingservice.infrastructure;

import dk.lw.loggingservice.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {
}
