package com.Abran.SpringSecurity.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

	private String email;
	   private String password;
	
}
