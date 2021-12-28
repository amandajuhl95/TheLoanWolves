package dk.lw.loanwolvesservice.DTO.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stubs.user.Address;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String number;

    @NotBlank(message = "Street name must be provided")
    @Size(min=4, message = "Street name must be at least 4 characters")
    private String street;

    @NotBlank(message = "City must be provided")
    @Size(min=2, message = "City name must be at least 2 characters")
    private String city;

    @Min(value = 1000, message = "Zipcode must consist of 4 digits")
    private int zipcode;

    public AddressDTO(Address address) {
        this.number = address.getNumber();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.zipcode = address.getZipcode();
    }
}