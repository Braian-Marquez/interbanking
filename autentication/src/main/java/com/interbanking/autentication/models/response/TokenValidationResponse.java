package com.interbanking.autentication.models.response;

import java.util.List;

import lombok.Data;
@Data
public class TokenValidationResponse {
	private List<String> roles;
	public Long id;
}
