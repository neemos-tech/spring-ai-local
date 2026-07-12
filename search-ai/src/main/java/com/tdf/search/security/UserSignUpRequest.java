package com.tdf.search.security;

import lombok.Value;

@Value
public class UserSignUpRequest {
    String username;
    String password;
}
