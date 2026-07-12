package com.tdf.search.controllers;

import com.tdf.search.security.UserSignUpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    @PostMapping("/signup")
    public String signUp(@RequestBody UserSignUpRequest userSignUpRequest){

        return "User signed up successfully with username: " + userSignUpRequest.getUsername();
    }

}
