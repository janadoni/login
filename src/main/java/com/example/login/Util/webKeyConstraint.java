package com.example.login.Util;

import org.springframework.stereotype.Component;

@Component
public class webKeyConstraint {
    public static final String requestMapping="/api/cafe";
    public static final String generateToken="/generatetoken";
    public static final String login="/login";
    public static final String logout="/logout";
    public static final String register="/createuser";
    public static final String forgetPassword="/forgetpassword";


}
