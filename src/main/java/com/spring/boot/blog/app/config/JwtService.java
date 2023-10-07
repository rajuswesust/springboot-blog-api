package com.spring.boot.blog.app.config;

import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.exception.BlogApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "2001db428d7468b6d41355a7e4a573ed22b73e68ca91118398a85b9b38f38817";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T>T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseClaimsJws(token).getPayload();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        User user = (User) userDetails;
        extraClaims.put("id", user.getId());
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        System.out.println("generate token: " + userDetails);
        return Jwts.builder().claims(extraClaims).
                subject(userDetails.getUsername()).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(convertMillisToDate(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSignInKey(), Jwts.SIG.HS256).compact();
    }

    public static Date convertMillisToDate(long millis) {
        return new Date(millis);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT sequence");
        } catch (MalformedJwtException ex) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (BlogApiException ex) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT");
        } catch (IllegalArgumentException ex) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }


    }
}
