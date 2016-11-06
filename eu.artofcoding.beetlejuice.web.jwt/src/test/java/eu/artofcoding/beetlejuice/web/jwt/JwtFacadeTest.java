package eu.artofcoding.beetlejuice.web.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Test;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.assertEquals;

public class JwtFacadeTest {

    @Test
    public void shouldCreateTokenAndReadSubject() throws Exception {
        final JwtFacade jwtFacade = new JwtFacade();
        final SecretKeySpec secretKey = jwtFacade.makeSecretKey("abc123".getBytes());
        final String token = jwtFacade.makeToken(secretKey, "joe", 10);
        final Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        final Claims body = claimsJws.getBody();
        assertEquals(body.getSubject(), "joe");
    }

}
