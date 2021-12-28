package dk.lw.loginservice;


import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;

public class AppSettings {

    public static final String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEKChokoBaNan";
    public static final String issuer = "LoanWolves";

    public static final Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());

    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public static final SimpleDateFormat cprFormatter = new SimpleDateFormat("ddMMyy");

    public static final String serviceName = "LoginService";

}
