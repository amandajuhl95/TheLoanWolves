package dk.lw.loanwolvesservice.DTO.loan;


import dk.lw.loanwolvesservice.DTO.login.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {

    private LoanUserDTO user;
    private double amount;

    public LoanRequestDTO(double amount, UserDTO user) {
        this.amount = amount;
        this.user = new LoanUserDTO(user);
    }

}
