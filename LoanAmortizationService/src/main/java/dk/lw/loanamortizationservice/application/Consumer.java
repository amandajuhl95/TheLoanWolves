package dk.lw.loanamortizationservice.application;

import com.google.gson.Gson;
import dk.lw.loanamortizationservice.AppSettings;
import dk.lw.loanamortizationservice.DTO.AmortizationDTO;
import dk.lw.loanamortizationservice.DTO.LoanAdmissionDTO;
import dk.lw.loanamortizationservice.DTO.LoanDTO;
import dk.lw.loanamortizationservice.domain.Amortization;
import dk.lw.loanamortizationservice.domain.Loan;
import dk.lw.loanamortizationservice.domain.LoanQuote;
import dk.lw.loanamortizationservice.domain.Status;
import dk.lw.loanamortizationservice.infrastructure.LoanQuoteRepository;
import dk.lw.loanamortizationservice.infrastructure.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Consumer {

    private final Gson gson = new Gson();

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanQuoteRepository loanQuoteRepository;

    @Autowired
    private Producer producer;

    @Autowired
    private Logger logger;

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
            String error = "Loan (" + amortizationDTO.getLoanId() + ") is not found";
            logger.sendLogs(AppSettings.serviceName, error, HttpStatus.NOT_FOUND.value());
            
            producer.transferDenied(amortizationDTO);
        }
    }

    @KafkaListener(topics = "loan-decision", groupId = "loan")
    public void consumeLoan(String request)
    {
        LoanAdmissionDTO loanAdmissionDTO = gson.fromJson(request, LoanAdmissionDTO.class);
        Optional<LoanQuote> quote = loanQuoteRepository.findById(loanAdmissionDTO.getLoanQuoteId());

        if(quote.isPresent()) {
            LoanQuote loanquote = quote.get();

            if(loanquote.getUserId().equals(loanAdmissionDTO.getUserId())) {
                if(loanquote.getStatus().equals(Status.PENDING))  {

                    if(loanAdmissionDTO.getStatus().equals(Status.ACCEPTED)) {
                        Loan loan = new Loan(loanquote);
                        loan = loanRepository.save(loan);

                        LoanDTO loanDTO = new LoanDTO(loan);
                        producer.transferLoan(loanDTO);

                        loanquote.setStatus(Status.ACCEPTED);
                        loanQuoteRepository.save(loanquote);
                    }
                    else if(loanAdmissionDTO.getStatus().equals(Status.DECLINED)) {
                        loanquote.setStatus(Status.DECLINED);
                        loanQuoteRepository.save(loanquote);
                    } else {
                        String error = "Loan quote decision must be declined or accepted";
                        logger.sendLogs(AppSettings.serviceName, error, HttpStatus.NOT_ACCEPTABLE.value());
                     }
                } else{
                String error = "Loan quote have already been declined or accepted";
                logger.sendLogs(AppSettings.serviceName, error, HttpStatus.ALREADY_REPORTED.value());
                }
            } else{
            String error = "Loan quote is not offered to user " + loanAdmissionDTO.getUserId();
            logger.sendLogs(AppSettings.serviceName, error, HttpStatus.BAD_REQUEST.value());
            }
        } else{
        String error = "Loan quote (" + loanAdmissionDTO.getLoanQuoteId() + ") is not found";
        logger.sendLogs(AppSettings.serviceName, error, HttpStatus.NOT_FOUND.value());
        }
    }

}
