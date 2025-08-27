package com.interbanking.autentication.infrastructure.adapter.out.persistence;

import com.interbanking.autentication.domain.port.out.TokenService;
import com.interbanking.commons.models.entity.Role;
import com.interbanking.commons.models.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TokenServiceAdapter implements TokenService {
    private static final String SECRET_KEY = "mySecretKeymySecretKeymySecretKeymySecretKey";
    private static final long JWT_EXPIRATION = 86400000; 

    @Override
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        claims.put("userId", user.getId());
        
        return createToken(claims, user.getUsername());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserEntity getUserFromToken(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        String email = (String) claims.get("email");
        Long userId = ((Number) claims.get("userId")).longValue();
        
        @SuppressWarnings("unchecked")
        List<String> roleNames = (List<String>) claims.get("roles");
        List<Role> roles = roleNames.stream()
            .map(roleName -> new Role(null, roleName, null))
            .collect(Collectors.toList());

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail(email);
        user.setUsername(username);
        user.setRoles(roles);

        return user;
    }

    @Override
    public String refreshToken(String token) {
        UserEntity user = getUserFromToken(token);
        return generateToken(user);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

 

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}