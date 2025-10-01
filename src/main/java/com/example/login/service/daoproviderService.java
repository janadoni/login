package com.example.login.service;

import com.example.login.repository.loginrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class daoproviderService implements UserDetailsService {
    @Autowired
    public loginrepo loginrepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginrepo.findByUserName(username);
    }
}
