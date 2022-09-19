package roommateproject.roommatebackend.token;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class JwtTokenProvider {

    @Value("${spring.encrypt.password}")
    private String encrypt;
    @Value("${spring.encrypt.secret")
    private String secret;
    private String secretKey;
    private String secretRefresh;

    public boolean validateToken(String jwtToken) {
        try {
            secretKey = Base64.getEncoder().encodeToString(encrypt.getBytes());
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateRefreshToken(String jwtToken) {
        try {
            secretRefresh = Base64.getEncoder().encodeToString(secret.getBytes());
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretRefresh).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String createToken(Long id) {
        Date now = new Date();
        Claims claims = Jwts.claims();

        secretKey = Base64.getEncoder().encodeToString(encrypt.getBytes());
        secretRefresh = Base64.getEncoder().encodeToString(secret.getBytes());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (1000L * 60 * 30)))
                .setId(Long.toString(id))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact() + " " +
                Jwts.builder()
                        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + (1000L * 60 * 60 * 24 * 10)))
                        .setId(Long.toString(id))
                        .signWith(SignatureAlgorithm.HS256, secretRefresh)
                        .compact();
    }

    public Claims getInformation(String token) {
        secretKey = Base64.getEncoder().encodeToString(encrypt.getBytes());
        Claims claims =Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims;
    }

}
