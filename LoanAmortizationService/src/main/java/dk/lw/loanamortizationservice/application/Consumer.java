package dk.lw.loanamortizationservice.application;

import com.google.gson.Gson;
import dk.lw.loanamortizationservice.DTO.AmortizationDTO;
import dk.lw.loanamortizationservice.DTO.LoanAdmissionDTO;
import dk.lw.loanamortizationservice.DTO.LoanDTO;
import dk.lw.loanamortizationservice.domain.Amortization;
import dk.lw.loanamortizationservice.domain.Loan;
import dk.lw.loanamortizationservice.domain.LoanQuote;
import dk.lw.loanamortizationservice.infrastructure.AmortizationRepository;
import dk.lw.loanamortizationservice.infrastructure.LoanQuoteRepository;
import dk.lw.loanamortizationservice.infrastructure.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Consumer {

    private final Gson gson = new Gson();

    @Autowired
    private AmortizationRepository amortizationRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanQuoteRepository loanQuoteRepository;

    @Autowired
    private Producer producer;

    @KafkaListener(topics = "amortization-request", groupId = "loan")
    public void consumePayment(String request)
    {
        AmortizationDTO amortizationDTO = gson.fromJson(request, AmortizationDTO.class);
        Optional<Loan> optionalLoan = loanRepository.findById(amortizationDTO.getLoanId());

        if(optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();

            Amortization amortization = new Amortization(amortizationDTO, loan);
            loan.addAmortization(amortization);
            loanRepository.save(loan);
        } else {
            producer.transferDenied(amortizationDTO);
        }
    }

    @KafkaListener(topics = "loan-accept", groupId = "loan")
    public void consumeLoan(String request)
    {
        LoanAdmissionDTO loanAdmissionDTO = gson.fromJson(request, LoanAdmissionDTO.class);
        Optional<LoanQuote> quote = loanQuoteRepository.findById(loanAdmissionDTO.getLoanQuoteId());

        if(quote.isPresent()) {
            LoanQuote loanquote = quote.get();

            if(loanquote.getUserId().equals(loanAdmissionDTO.getUserId())) {

                Loan loan = new Loan(loanquote);
                loan = loanRepository.save(loan);

                LoanDTO loanDTO = new LoanDTO(loan);
                producer.transferLoan(loanDTO);
            }
        }
    }

}
