package dk.lw.loanwolvesservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {

    private LoanUserDTO user;
    private double amount;

}
