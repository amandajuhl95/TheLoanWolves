package dk.lw.accountservice.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidTransactionException extends Exception {
    public InvalidTransactionException() {
        super();
    }
    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidTransactionException(String message) {
        super(message);
    }

}
