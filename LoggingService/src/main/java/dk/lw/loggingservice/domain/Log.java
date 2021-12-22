package dk.lw.loggingservice.domain;

import dk.lw.loggingservice.DTO.LogDTO;
import dk.lw.loggingservice.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    private UUID id;
    private String serviceName, errorMessage, date;
    private int errorCode;

    public Log(LogDTO logDTO) {
        this.id = UUID.randomUUID();
        Date date = new Date();
        this.serviceName = logDTO.getServiceName();
        this.errorMessage = logDTO.getErrorMessage();
        this.errorCode = logDTO.getErrorCode();
        this.date = Utils.formatter.format(date);
    }

}
