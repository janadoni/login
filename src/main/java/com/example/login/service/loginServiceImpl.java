package com.example.login.service;

import com.example.login.entity.login;
import com.example.login.repository.loginrepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.OAuth2LoginDsl;
import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class loginServiceImpl implements loginService{
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public loginrepo loginrepo;
    @Autowired
    public JWTFilterService jwtFilterService;
    @Autowired
    public AuthenticationManager authenticationManager;

    public Mono<String> generateToken(login login) throws RuntimeException, Exception {
       return Mono.fromCallable(()-> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword())))
                .flatMap(authentication -> {
                            LocalDateTime now =LocalDateTime.now();

                    return Mono.fromCallable(()-> {
                         return jwtFilterService.generateToken(login);

                    }).subscribeOn(Schedulers.boundedElastic());
                }
                ).onErrorResume(e -> Mono.error(new RuntimeException("Invalid Username or password",e)));

    }

    @Override
    public Mono<login> registry(login Login) throws Exception {
       return  Mono.fromCallable(() ->{
           Login.setPassword(passwordEncoder.encode(Login.getPassword()));
           Login.setCreatedAt(LocalDate.now());
           Login.setUpdateAt(null);
           return loginrepo.save(Login);
       }).subscribeOn(Schedulers.boundedElastic());
    }






}
