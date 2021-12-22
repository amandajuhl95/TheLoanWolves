package dk.lw.loginservice.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Data
@Entity
public class Address {

    @Id
    private UUID id;
    private String number, street, city;

    @Column(length = 4)
    private int zipcode;

    public Address() {}

    public Address(String number, String street, String city, int zipcode) {
        this.id = UUID.randomUUID();
        this.number = number;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    public Address(stubs.user.Address address) {
        this.id = UUID.randomUUID();
        this.number = address.getNumber();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.zipcode = address.getZipcode();
    }
}