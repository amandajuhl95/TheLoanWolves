package dk.lw.loanwolvesservice.application;

import dk.lw.loanwolvesservice.DTO.LoginDTO;
import dk.lw.loanwolvesservice.DTO.UserDTO;
import dk.lw.loanwolvesservice.Utils;
import dk.lw.loanwolvesservice.errorhandling.LoginException;
import dk.lw.loanwolvesservice.errorhandling.UnauthorizedException;
import dk.lw.loanwolvesservice.infrastructure.LoginClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan-wolves")
public class LoginController {

    private LoginClient loginClient = new LoginClient();

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO request) throws LoginException {
        String token = loginClient.login(request);
        if(token.isEmpty()) {
            throw new LoginException("Invalid login");
        }
        return token;
    }

    @PostMapping("/new/user")
    public UserDTO createUser(@RequestBody UserDTO user) {
        user = loginClient.createUser(user);
        return user;
    }

    @PutMapping("/update/user")
    public UserDTO updateUser(@RequestBody UserDTO user, @RequestHeader("Session-Token") String token) throws UnauthorizedException {

        if(Utils.validToken(token) && Utils.authorize(token, user.getId()))
        {
            user = loginClient.updateUser(user);
            return user;
        }
        throw new UnauthorizedException("Login expired or unauthorized for this action");
    }

    @GetMapping("/user/{cpr}")
    public UserDTO getUser(@PathVariable String cpr, @RequestHeader("Session-Token") String token) throws UnauthorizedException {

        if(Utils.validToken(token)) {
            UserDTO user = loginClient.getUser(cpr);
            if(Utils.authorize(token, user.getId()))
            {
                return user;
            }
            throw new UnauthorizedException("Unauthorized for this action");
        }
        throw new UnauthorizedException("Login expired");
    }
}
