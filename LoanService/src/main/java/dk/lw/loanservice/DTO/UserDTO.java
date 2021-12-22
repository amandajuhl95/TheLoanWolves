package dk.lw.loanservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String fullName, email, cpr;
    private int age;
    private double salary;
    private AddressDTO address;

}


