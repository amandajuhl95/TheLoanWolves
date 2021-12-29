package dk.lw.loanservice.application;

import com.google.gson.Gson;
import dk.lw.loanservice.DTO.AmortizationDTO;
import dk.lw.loanservice.DTO.LoanQuoteStatus;
import dk.lw.loanservice.DTO.TransactionDTO;
import dk.lw.loanservice.DTO.LoanAdmissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Producer {

    private final Gson gson = new Gson();

    private static final String TOPIC = "loan-decision";
    private static final String AMORTIZATION_TOPIC = "amortization-request";

    @Autowired
    private KafkaTemplate<String, String> requestTemplate;

    public void sendLoan(LoanQuoteStatus status, UUID userId, UUID loanQuoteId)
    {
        LoanAdmissionDTO loanAdmissionDTO = new LoanAdmissionDTO(userId, loanQuoteId, status);
        requestTemplate.send(TOPIC, gson.toJson(loanAdmissionDTO));
        requestTemplate.flush();
    }

    public void sendAmortization(TransactionDTO transactionDTO, UUID loadId)
    {
        AmortizationDTO amortizationDTO = new AmortizationDTO(transactionDTO, loadId);
        requestTemplate.send(AMORTIZATION_TOPIC, gson.toJson(amortizationDTO));
        requestTemplate.flush();
    }


}
