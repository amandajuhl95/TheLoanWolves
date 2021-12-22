package dk.lw.loanquoteservice.application;

import com.google.gson.Gson;
import dk.lw.loanquoteservice.domain.LoanQuote;
import dk.lw.loanquoteservice.infrastructure.LoanQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private final Gson gson = new Gson();

    @Autowired
    private LoanQuoteRepository loanQuoteRepository;

    @KafkaListener(topics = "save-loan-quote")
    public void consumeLoanQuote(String request)
    {
        LoanQuote loanQuote = gson.fromJson(request, LoanQuote.class);
        loanQuoteRepository.save(loanQuote);
    }
}
