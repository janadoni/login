package com.example.login.contoller;

import com.example.login.entity.login;
import com.example.login.service.JWTFilterService;
import com.example.login.service.loginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.login.Util.webKeyConstraint;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = webKeyConstraint.requestMapping)
public class loginController {
    @Autowired
    private loginServiceImpl loginServiceImpl;
    @Autowired
    private JWTFilterService jwtFilterService;
    @PostMapping(webKeyConstraint.generateToken)
    public Mono<ResponseEntity<?>> generateToken(@RequestBody login login) throws Exception {
        return loginServiceImpl.generateToken(login).map(token->{return new ResponseEntity<String>(token,HttpStatus.OK);});
    }
    @PostMapping(webKeyConstraint.register)
    public Mono<ResponseEntity<?>> registryUser(@RequestBody login login) throws Exception {
    return loginServiceImpl.registry(login).map(response -> {return new ResponseEntity<login>(response,HttpStatus.OK);} );
    }
    @GetMapping("/dashboard")
    public Mono<ResponseEntity<?>> registryUser1() throws Exception {
         return Mono.just(new ResponseEntity<String>("Ok workings",HttpStatus.OK));
    }

}
