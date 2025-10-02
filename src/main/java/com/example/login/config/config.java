package com.example.login.config;

import com.example.login.service.CustomOAuth2UserService;
import com.example.login.service.daoproviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class config {
    @Autowired
    public daoproviderService daoproviderService;
    @Autowired
    public JWTAuthenticationfilter jwtAuthenticationfilter;
    @Autowired
    public OauthSuccessHandler oauthSuccessHandler;
    @Autowired
    public CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(15);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for H2 consol
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // allow frames
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**","/api/cafe/generatetoken", "/api/cafe/createuser",
                                "/login/oauth2/code/google","/oauth2/authorization/google","/login", "/default-ui.css", "/favicon.ico").permitAll() // allow H2 console
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOAuth2UserService)
                        )
                        .successHandler(oauthSuccessHandler))
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
               .addFilterBefore(jwtAuthenticationfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(daoproviderService);
        return daoAuthenticationProvider;
    }

}
