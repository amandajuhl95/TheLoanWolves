package dk.lw.loanwolvesservice.application;

import dk.lw.loanwolvesservice.DTO.account.AccountDTO;
import dk.lw.loanwolvesservice.DTO.login.CreateUserDTO;
import dk.lw.loanwolvesservice.DTO.login.LoginDTO;
import dk.lw.loanwolvesservice.DTO.login.UpdateUserDTO;
import dk.lw.loanwolvesservice.DTO.login.UserDTO;
import dk.lw.loanwolvesservice.Utils;
import dk.lw.loanwolvesservice.errorhandling.LoginException;
import dk.lw.loanwolvesservice.errorhandling.UnauthorizedException;
import dk.lw.loanwolvesservice.infrastructure.LoginClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/loan-wolves")
public class LoginController {

    private LoginClient loginClient = new LoginClient();

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDTO request) throws LoginException {
        String token = loginClient.login(request);
        return token;
    }

    @PostMapping("/new/user")
    public UserDTO createUser(@RequestBody @Valid CreateUserDTO userDTO) {
        UserDTO user = loginClient.createUser(userDTO);
        return user;
    }

    @PutMapping("/update/user")
    public UserDTO updateUser(@RequestBody @Valid UpdateUserDTO userDTO, @RequestHeader("Session-Token") String token) throws UnauthorizedException {

        if(Utils.validToken(token)) {
            if(Utils.authorize(token, userDTO.getId()))
            {
                UserDTO user = loginClient.updateUser(userDTO);
                return user;
            }
            throw new UnauthorizedException("Unauthorized for this action");
        }
        throw new UnauthorizedException("Login expired");
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
