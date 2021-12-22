package dk.lw.loginservice.domain;

import dk.lw.loginservice.AppSettings;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;
import stubs.user.CreateUserRequest;
import javax.persistence.*;
import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Entity
public class User {
    @Id
    private UUID id;

    @Column(length = 11, unique = true)
    private String cpr;

    @Column(length = 8, unique = true)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(unique = true)
    private String email;
    private String password, fullName;
    private Type type;
    private double salary;

    public User() {}

    public User(String cpr, String phoneNumber, String email, String password, String fullName, Type type,
                double salary, Address address) {
        this.id = UUID.randomUUID();
        this.cpr = cpr;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.password = hash(password);
        this.fullName = fullName;
        this.type = type;
        this.salary = salary;
    }

    public User(CreateUserRequest userRequest) {
        this.id = UUID.randomUUID();
        this.cpr = userRequest.getCpr();
        this.phoneNumber = userRequest.getPhoneNumber();
        this.address = new Address(userRequest.getAddress());
        this.email = userRequest.getEmail();
        this.password = hash(userRequest.getPassword());
        this.fullName = userRequest.getFullName();
        this.type = Type.valueOf(userRequest.getType().name());
        this.salary = userRequest.getSalary();
    }

    public void setPassword(String password) {
        this.password = hash(password);
    }

    public int getAge() throws ParseException {
        String birthday = cpr.split("-")[0];
        Date birthDate = AppSettings.cprFormatter.parse(birthday);
        Date today = new Date();

        long diff = Math.abs(today.getTime() - birthDate.getTime());
        int age = (int)(diff / (1000l*60*60*24*365));

        return age;
    }

    private String hash(String password) {
        password += "chokobanan";
        long sum = 0, mul = 1;
        for (int i = 0; i < password.length(); i++) {
            mul = (i % 4 == 0) ? 1 : mul * 256;
            sum += password.charAt(i) * mul;
        }
        return Long.toString(Math.abs(sum) % 99999);
    }

    public boolean validPassword(String password) {
        return this.password.equals(hash(password));
    }

    public String generateToken() {
        Date now = new Date();
        Date expire = DateUtils.addMinutes(now, 30);

        String jwtToken = Jwts.builder()
                .claim("name", fullName)
                .claim("id", id)
                .claim("type", type.toString())
                .setSubject(id.toString())
                .setId(UUID.randomUUID().toString())
                .setIssuer(AppSettings.issuer)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(AppSettings.hmacKey)
                .compact();

        return jwtToken;
    }
}
