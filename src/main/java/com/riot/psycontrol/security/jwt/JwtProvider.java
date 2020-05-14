package com.riot.psycontrol.security.jwt;

import com.riot.psycontrol.dao.Role;
import com.riot.psycontrol.security.CustomException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtProvider {

    private String secretKey = "SuperSecreto";
    private long validityInMilliseconds = 60*60000;//1H 3600000;

    @Autowired
    private UserDetailsService userDetailsServiceDetails;

    public String createToken(String username, List<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        //claims.put("auth", grantedAuthorityList);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new CustomException("Invalid JWT Signature", HttpStatus.UNAUTHORIZED);
        }catch (MalformedJwtException ex) {
            throw new CustomException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }catch (ExpiredJwtException ex) {
            throw new CustomException("Expired JWT token", HttpStatus.UNAUTHORIZED);
        }catch (UnsupportedJwtException ex) {
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
        UserDetails userDetails = userDetailsServiceDetails.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


}
