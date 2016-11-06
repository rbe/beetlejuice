package eu.artofcoding.beetlejuice.web.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtFacade {

    private static final String hmacSHA256 = "HmacSHA256";

    public SecretKeySpec makeSecretKey(final byte[] secret) throws JwtSecretKeyException {
        final Mac sha256Hmac;
        try {
            sha256Hmac = Mac.getInstance(hmacSHA256);
        } catch (NoSuchAlgorithmException e) {
            throw new JwtSecretKeyException(e);
        }
        final SecretKeySpec secretKey = new SecretKeySpec(secret, hmacSHA256);
        try {
            sha256Hmac.init(secretKey);
        } catch (InvalidKeyException e) {
            throw new JwtSecretKeyException(e);
        }
        return secretKey;
    }

    public String makeToken(final SecretKeySpec secretKeySpec, final String subject, final int expirationInSeconds) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(Date.from(Instant.now().plus(expirationInSeconds, ChronoUnit.SECONDS)))
                .signWith(SignatureAlgorithm.HS512, secretKeySpec)
                .compact();
    }

}
