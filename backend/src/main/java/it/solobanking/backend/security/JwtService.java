package it.solobanking.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String CHIAVE_SEGRETA = "AA1OO125AB11O422BB44R0NXXXYYY16KKKJJJF1NAL";

    // Durata token 24 ore
    private static final long TEMPISTICA = 1000 * 60 * 60 * 24;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(CHIAVE_SEGRETA.getBytes());
    }

    public String generaToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEMPISTICA))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String estraiEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
