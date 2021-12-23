package dk.lw.loanwolvesservice.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        super();
    }
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException(Throwable cause) {
        super(cause);
    }
}
