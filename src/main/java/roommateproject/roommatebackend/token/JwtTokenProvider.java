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
    private String secretKey;

    public String createToken(String email) {
        Date now = new Date();
        Claims claims = Jwts.claims();

        secretKey = Base64.getEncoder().encodeToString(encrypt.getBytes());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (1000L * 60 * 30)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            secretKey = Base64.getEncoder().encodeToString(encrypt.getBytes());
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("ex : {}",e.getMessage());
            return false;
        }
    }

    public Claims getInformation(String token) {
        secretKey = Base64.getEncoder().encodeToString(encrypt.getBytes());
        Claims claims =Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims;
    }

}
