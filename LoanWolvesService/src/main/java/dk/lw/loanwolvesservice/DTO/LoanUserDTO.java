package dk.lw.loanwolvesservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanUserDTO {
    private UUID id;
    private String fullName, email, cpr;
    private int age;
    private double salary;
    private AddressDTO address;

    public LoanUserDTO(UserDTO user) {
        this.id = user.getId();
        this.cpr = user.getCpr();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.salary = user.getSalary();
        this.address = user.getAddress();
    }
}
