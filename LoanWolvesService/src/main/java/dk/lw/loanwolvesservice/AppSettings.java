package dk.lw.loanwolvesservice;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;

public class AppSettings {
    public static final String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEKChokoBaNan";
    public static final String issuer = "LoanWolves";

    public static final String accountServiceURL = "http://localhost:8060/account/";
    public static final String loanServiceURL = "http://localhost:8091/loan/";

    public static final Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());


}
