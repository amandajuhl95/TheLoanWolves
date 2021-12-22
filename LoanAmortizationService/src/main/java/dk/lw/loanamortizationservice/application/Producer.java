package dk.lw.loanamortizationservice.application;

import com.google.gson.Gson;
import dk.lw.loanamortizationservice.DTO.AmortizationDTO;
import dk.lw.loanamortizationservice.DTO.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private final Gson gson = new Gson();
    private static final String TOPIC = "loan-transfer";
    private static final String DENIED_TOPIC = "denied-transfer";

    @Autowired
    private KafkaTemplate<String, String> requestTemplate;

    public void transferLoan(LoanDTO loanDTO)
    {
        requestTemplate.send(TOPIC, gson.toJson(loanDTO));
        requestTemplate.flush();
    }

    public void transferDenied(AmortizationDTO amortizationDTO)
    {
        requestTemplate.send(DENIED_TOPIC, gson.toJson(amortizationDTO));
        requestTemplate.flush();
    }


}
