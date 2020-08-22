package com.riot.psycontrol.util.jwt;

import com.riot.psycontrol.util.CustomException;
import com.riot.psycontrol.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtProvider {

    @Value("${config.jwt.secretKey}")
    private String secretKey;
    @Value("${config.jwt.validityInMilliseconds}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    /**
     * @param username passed from userService
     * @return generated Json Web Token into String
     */
    public String createToken(@NotNull String username) {
        var user = userService.getUserByUsername(username);
        if (user != null) {
            var claims = Jwts.claims().setSubject(username);
            claims.put("username", user.getUsername());
            claims.put("firstname", user.getFirstname());
            claims.put("lastname", user.getLastname());
            claims.put("email", user.getEmail());
            claims.put("roles", user.getRoles());
            var now = new Date();
            var validity = new Date(now.getTime() + validityInMilliseconds);

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        } else
            throw new CustomException("User does not exist", HttpStatus.BAD_REQUEST);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new CustomException("Invalid JWT Signature", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException ex) {
            throw new CustomException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException ex) {
            throw new CustomException("Expired JWT token", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException("Unsupported JWT token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException ex) {
            throw new CustomException("Illegal string token", HttpStatus.UNAUTHORIZED);
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Authentication getAuthentication(String token) {
        var userDetails = userDetailsService.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
