package dk.lw.loanwolvesservice.DTO.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stubs.user.UserResponse;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private UUID id;
    private String fullName, email, phoneNumber, cpr;
    private int age;
    private double salary;
    private String type;
    private AddressDTO address;

    public UserDTO(UserResponse response) {
        this.id = UUID.fromString(response.getId());
        this.cpr = response.getCpr();
        this.fullName = response.getFullName();
        this.email = response.getEmail();
        this.phoneNumber = response.getPhoneNumber();
        this.age = response.getAge();
        this.salary = response.getSalary();
        this.type = response.getType().toString();
        this.address = new AddressDTO(response.getAddress());
    }
}



