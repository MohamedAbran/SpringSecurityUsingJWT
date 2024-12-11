package com.Abran.SpringSecurity.Response;

import java.util.List;

import com.Abran.SpringSecurity.Entity.RefreshToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {

	private String refreshToken;
	private String accesstoken;
	private String type = "Bearer";
	private int id;
	private String username;
	private List<String> roles;

	public JwtResponse(String accesstoken,String refreshToken, int id, String username, List<String> rols) {
		this.accesstoken = accesstoken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.roles = rols;
	}

	public JwtResponse(String accesstoken, String refreshToken, int id) {
		this.accesstoken = accesstoken;
		this.refreshToken = refreshToken;
		this.id = id;
	}

}
