package dk.lw.loantypesgateway;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanType {
    String type;
    double min_limit, max_limit, interest_rate,fee;
    int duration;


}
