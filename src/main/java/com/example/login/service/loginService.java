package com.example.login.service;

import com.example.login.entity.login;
import reactor.core.publisher.Mono;

public interface loginService {
    public Mono<login> registry(login Login)throws Exception;
}
