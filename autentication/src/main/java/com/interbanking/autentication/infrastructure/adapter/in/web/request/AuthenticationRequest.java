package com.interbanking.autentication.infrastructure.adapter.in.web.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}