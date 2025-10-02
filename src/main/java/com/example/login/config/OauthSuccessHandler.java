package com.example.login.config;

import com.example.login.entity.login;
import com.example.login.repository.loginrepo;
import com.example.login.service.JWTFilterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OauthSuccessHandler  implements AuthenticationSuccessHandler {
    @Autowired
    public JWTFilterService jwtFilterService;
    @Autowired
    public loginrepo loginrepo;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal =authentication.getPrincipal();
        String username=null;
        if(principal instanceof UserDetails){
            username=((UserDetails) principal).getUsername();
        }else {
            OAuth2User oAuth2User= (OAuth2User) principal;
            Object email=oAuth2User.getAttributes().get("email");
            username=email !=null ? email.toString():null;

        }
        if(username!=null){
            try {
                loginrepo.findByEmail(username);
                String token="Bearer "+jwtFilterService.generateToken(loginrepo.findByEmail(username));

                WebClient client=WebClient.builder()
                        .baseUrl("http://localhost:89")
                        .defaultHeader(HttpHeaders.AUTHORIZATION,token)
                        .build();
                String responses=client.get()
                        .uri("/api/cafe/dashboard")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
//
//                response.setHeader("Authorization",token);
                response.setContentType("application/json");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                response.getWriter().write("{\"token\":\"" + token + "\",\"redirectResponse\":" + responses + "}");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
