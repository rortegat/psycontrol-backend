package com.riot.psycontrol.security.jwt;

import com.riot.psycontrol.entity.Role;
import com.riot.psycontrol.security.CustomException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtProvider {

    @Value("${config.jwt.secretKey}")
    private String secretKey;
    @Value("${config.jwt.validityInMilliseconds}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsServiceDetails;

    /**
     *
     * @param username passed from userService
     * @param roles user permissions extracted from database
     * @return Generated Json Web Token
     */
    public String createToken(String username, List<Role> roles) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        roles.forEach( role ->{
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRolename()));
        });
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", grantedAuthorityList);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
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


}
