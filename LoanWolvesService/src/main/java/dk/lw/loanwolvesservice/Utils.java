package dk.lw.loanwolvesservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import stubs.user.Type;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {

    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public static boolean validToken(String token) {
        Jws<Claims> jwt = decodeToken(token);
        Date expiration = jwt.getBody().getExpiration();
        Date now = new Date();

        if(expiration.after(now) && jwt.getBody().getIssuer().equals(AppSettings.issuer)) {
            return true;
        }
        return false;
    }

    public static boolean authorize(String token, UUID userId) {
        Jws<Claims> jwt = decodeToken(token);
        String type = jwt.getBody().get("type", String.class);
        String id = jwt.getBody().get("userId", String.class);

        return (type.equals(Type.EMPLOYEE.toString()) || id.equals(userId.toString()));
    }

    public static Jws<Claims> decodeToken(String token) {
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(AppSettings.hmacKey)
                .build()
                .parseClaimsJws(token);

        return jwt;
    }
}
