package com.Abran.SpringSecurity.Request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
	
	private String email;
	   private String password;
	   private Set<String> role;

}
