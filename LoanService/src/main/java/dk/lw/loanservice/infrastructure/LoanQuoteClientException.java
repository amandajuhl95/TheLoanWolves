package dk.lw.loanservice.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoanQuoteClientException extends Exception {
    public LoanQuoteClientException() {
        super();
    }
    public LoanQuoteClientException(String message, Throwable cause) {
        super(message, cause);
    }
    public LoanQuoteClientException(String message) {
        super(message);
    }
    public LoanQuoteClientException(Throwable cause) {
        super(cause);
    }
}
