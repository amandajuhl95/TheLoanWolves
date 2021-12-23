package dk.lw.loanwolvesservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stubs.user.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String number, street, city;
    private int zipcode;

    public AddressDTO(Address address) {
        this.number = address.getNumber();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.zipcode = address.getZipcode();
    }
}