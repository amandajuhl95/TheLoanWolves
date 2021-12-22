package dk.lw.loginservice;

import dk.lw.loginservice.domain.Address;
import dk.lw.loginservice.domain.Type;
import dk.lw.loginservice.domain.User;
import dk.lw.loginservice.infrastructure.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

public class test {
    @Autowired
    private static UserRepository repository;

    public static void main(String[] args) throws ParseException {
        Address address = new Address("21",  "Ejboparken",  "Roskilde", 4000 );
        User user = new User( "270744-0284",  "22222222", "amalie@mail.dk", "1234","Amalie Landt", Type.CUSTOMER, 44000, address);

        System.out.println(user.getAge());
        /*String token = user.generateToken();
        System.out.println(token);

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(AppSettings.secret),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);

        System.out.println(jwt);
        Date date = jwt.getBody().getExpiration();

        Date now = new Date();
        Date expire = DateUtils.addMinutes(now, 30);

        if(date.before(expire)) {
            System.out.println("token is not valid: " + date);
        }*/
    }
}
