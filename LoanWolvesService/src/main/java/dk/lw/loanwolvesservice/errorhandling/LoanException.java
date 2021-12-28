package dk.lw.loanwolvesservice.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoanException extends Exception {
    public LoanException() {
        super();
    }
    public LoanException(String message, Throwable cause) {
        super(message, cause);
    }
    public LoanException(String message) {
        super(message);
    }
    public LoanException(Throwable cause) {
        super(cause);
    }
}
