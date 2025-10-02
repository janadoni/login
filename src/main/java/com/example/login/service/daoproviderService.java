package com.example.login.service;

import com.example.login.entity.login;
import com.example.login.entity.role;
import com.example.login.repository.loginrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Service
public class daoproviderService implements UserDetailsService {
    @Autowired
    public loginrepo loginrepo;
//    @Autowired
//    public PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginrepo.findByUserName(username);
    }
    public login craeteOauthUser(String name, String email, role Role) throws Exception {

            login Login=new login();
            Login.setUserName(name);
            Login.setEmail(email);
            Login.setRole(Role);
            Login.setPassword(new BCryptPasswordEncoder().encode("OauthUser"));
            Login.setCreatedAt(LocalDate.now());
            Login.setUpdateAt(null);
            return loginrepo.save(Login);

    }
}
