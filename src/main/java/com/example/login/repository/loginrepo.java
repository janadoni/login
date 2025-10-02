package com.example.login.repository;

import com.example.login.entity.login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface loginrepo extends JpaRepository<login,Long>{

    public UserDetails findByUserName(String username);
    public login findByEmail(String username);
}
