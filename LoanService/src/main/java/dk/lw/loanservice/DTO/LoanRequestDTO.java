package dk.lw.loanservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {

    private UserDTO user;
    private double amount;

}
