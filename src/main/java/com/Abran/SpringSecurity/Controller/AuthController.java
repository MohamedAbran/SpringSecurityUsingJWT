package com.Abran.SpringSecurity.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Abran.SpringSecurity.Entity.RefreshToken;
import com.Abran.SpringSecurity.Jwt.JwtUtils;
import com.Abran.SpringSecurity.Repository.RoleRepo;
import com.Abran.SpringSecurity.Repository.UserRepo;
import com.Abran.SpringSecurity.Request.LoginRequest;
import com.Abran.SpringSecurity.Request.RefreshTokenRequest;
import com.Abran.SpringSecurity.Request.SignUpRequest;
import com.Abran.SpringSecurity.Response.JwtResponse;
import com.Abran.SpringSecurity.Security.Service.RefreshTokenService;
import com.Abran.SpringSecurity.Service.AuthService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/signup")
	public ResponseEntity<?> RegisterUser(@RequestBody SignUpRequest signUprequest) {
		return authService.RegisterUser(signUprequest);
	}

	@GetMapping("/Login")
	public ResponseEntity<?> AuthenticateUser(@RequestBody LoginRequest loginRequest) {
		return authService.AuthenticateUser(loginRequest);
	}

	@PostMapping("/refreshToken")
	public JwtResponse RefreshTokenCreate(@RequestBody RefreshTokenRequest refreshTokenRequest){
		return refreshTokenService.findByToken(refreshTokenRequest.getToken()).map(refreshTokenService::VerifyExpiration).map(RefreshToken::getU).map(u ->{
			String accessToken = jwtUtils.generateRefreshToken(u.getEmail());
			return new JwtResponse(accessToken,refreshTokenRequest.getToken(),u.getId());
			
			
		}).orElseThrow(() ->new RuntimeException("Refresh Token Is Not Db"));
	}

}
