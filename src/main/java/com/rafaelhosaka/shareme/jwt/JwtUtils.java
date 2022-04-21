package com.rafaelhosaka.shareme.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtUtils {
    private final long ACCESS_TOKEN_EXPIRE = 10 * 60 * 1000;
    private final long REFRESH_TOKEN_EXPIRE = 30 * 60 * 1000;

    public String createAccessToken(String username, String issuer, List<String> claim){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
                .withIssuer(issuer)
                .withClaim("roles",claim)
                .sign(getAlgorithm());
    }

    public String createRefreshToken(String username, String issuer){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
                .withIssuer(issuer)
                .sign(getAlgorithm());
    }

    public DecodedJWT decodeToken(String token){
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

    public Collection<SimpleGrantedAuthority> getAuthoritiesFromClaim(String[] roles){
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }

    public Map<String, String> createTokenMap(String accessToken, String refreshToken){
        Map<String, String> tokens =  new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    //TODO for now it is hardcoded
    public Algorithm getAlgorithm(){
        return  Algorithm.HMAC256("hmac256".getBytes());
    }
}
