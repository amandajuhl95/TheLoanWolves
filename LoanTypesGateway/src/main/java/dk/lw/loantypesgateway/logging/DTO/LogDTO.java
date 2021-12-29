package dk.lw.loantypesgateway.logging.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {

    private String serviceName, errorMessage;
    private int errorCode;

}
