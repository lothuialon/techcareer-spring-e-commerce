package com.lothuialon.userservice.security;

import java.security.Key;
import java.util.Date;

import com.lothuialon.userservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final String jwtSecret = "4367566C69703373367639798F423D4528482B4D6251655468576D5A71347437";
    private long jwtExpire = 43200000;

    public String generateToken(Authentication authentication, int userId) {

        //String username = authentication.getName();
        //User user = (User) authentication.getPrincipal();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpire);

        String token = Jwts.builder()
            .setSubject(Long.toString(userId)).setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(key())
            .compact();

        return token;

    }

    private Key key() {

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret));

    }

    // get username from token
    public String getUsername(String token) {

        String username = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        return username;

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parse(token);
            return true;
        } catch (MalformedJwtException exc) {
            throw new RuntimeException("Invalid token");
        } catch (ExpiredJwtException exc) {
            throw new RuntimeException("Expired token");
        } catch (UnsupportedJwtException exc) {
            throw new RuntimeException("Unsupported token");
        } catch (IllegalArgumentException exc) {
            throw new RuntimeException("Illegal token");
        }

    }

}