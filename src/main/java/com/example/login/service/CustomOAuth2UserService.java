package com.example.login.service;

import com.example.login.entity.role;
import com.example.login.repository.loginrepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {
@Autowired
public daoproviderService loginService;

    @Autowired
    public daoproviderService daoproviderService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // Call super to get the OIDC user
        OidcUser oidcUser = (OidcUser) super.loadUser(userRequest);

        String email = oidcUser.getEmail(); // OIDC standard attribute
        String name = oidcUser.getFullName(); // or getAttribute("name") if null

        // Optional: you can customize authorities
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // Save/update user in your DB
        if (email != null) {
            try {
                daoproviderService.craeteOauthUser(name, email,role.USER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

      return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}