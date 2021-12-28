package dk.lw.loanwolvesservice.DTO.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "CPR must be provided")
    @Pattern(message="CPR must be provided in pattern: xxxxxx-xxxx",
            regexp = "^\\d{6}-\\d{4}$", flags = Pattern.Flag.UNICODE_CASE)
    private String cpr;

    @NotBlank(message = "Password must be provided")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;
}
