package com.example.login.service;

import com.example.login.entity.login;
import com.example.login.repository.loginrepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;


@Service
public class JWTFilterService {
//    @Autowired
//    public PasswordEncoder passwordEncoder;
    @Autowired
    public com.example.login.repository.loginrepo loginrepo;

    @Value("${jwt.secret.key}")
    private String secretValue;
    @Value("${jwt.expiration.minutes}")
    private long expirationMinutes;
    private Key getSignKey(){
        byte[] key= Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secretValue.getBytes()));
        return Keys.hmacShaKeyFor(key);
    }
    private <T> T extractClaim(String token , Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey())
                .build().parseClaimsJws(token).getBody();
    }
    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean isTokenValid(String token, UserDetails details){
        final  String userName=extractUserName(token);
        return (userName.equals(details.getUsername()) && isTokenExpried(token));
    }
    public  boolean isTokenExpried(String Token){
        return extractClaim(Token,Claims::getExpiration).before(new Date());
    }


    public String generateToken(login login) throws RuntimeException, Exception {
        LocalDateTime now =LocalDateTime.now();

                                return Jwts.builder()
                                        .setSubject(login.getUsername())
                                        .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                                        .setExpiration(Date.from(now.plusMinutes(expirationMinutes).atZone(ZoneId.systemDefault()).toInstant()))
                                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secretValue.getBytes()))), SignatureAlgorithm.HS256)
                                        .compact();



    }


}
