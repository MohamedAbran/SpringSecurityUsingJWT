package com.Abran.SpringSecurity.Service;

import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Abran.SpringSecurity.Entity.ERole;
import com.Abran.SpringSecurity.Entity.RefreshToken;
import com.Abran.SpringSecurity.Entity.Role;
import com.Abran.SpringSecurity.Entity.user;
import com.Abran.SpringSecurity.Jwt.JwtUtils;
import com.Abran.SpringSecurity.Repository.RoleRepo;
import com.Abran.SpringSecurity.Repository.UserRepo;
import com.Abran.SpringSecurity.Request.LoginRequest;
import com.Abran.SpringSecurity.Request.SignUpRequest;
import com.Abran.SpringSecurity.Response.JwtResponse;
import com.Abran.SpringSecurity.Response.MessageResponse;
import com.Abran.SpringSecurity.Security.Service.RefreshTokenService;
import com.Abran.SpringSecurity.Security.Service.UserDetailImpl;

@Service
public class AuthService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private RoleRepo Rolerepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private RefreshTokenService refreshTokenService;

	public ResponseEntity<?> RegisterUser(SignUpRequest signUprequest) {

		if (userRepo.existsByemail(signUprequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error UserName is Already Taken"));
		}

		user u = new user(signUprequest.getEmail(), encoder.encode(signUprequest.getPassword()));

		Set<String> strRoles = signUprequest.getRole();
		Set<Role> roles = new HashSet<Role>();

		if (strRoles == null) {
			Role Userrols = Rolerepo.findByname(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role Is Not Found"));
			roles.add(Userrols);
		} else {
			strRoles.forEach(role -> {
				switch (role.toLowerCase()) {
				case "admin":
					Role adminRole = Rolerepo.findByname(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;

				case "mod":
					Role modRole = Rolerepo.findByname(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role UserRole = Rolerepo.findByname(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(UserRole);

				}
			});
		}

		u.setRole(roles);
		userRepo.save(u);

		return ResponseEntity.ok().body(new MessageResponse("Register Successfully"));

	}

	public ResponseEntity<?> AuthenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.GenarateToken(authentication);

		System.out.println("JWT::" + jwt);
		UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
		
		RefreshToken refreshToken = refreshTokenService.CreateRefreshToken(userDetails.getUsername());
		String ReToken =  refreshToken.getToken();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(new JwtResponse(jwt,ReToken	,userDetails.getId(), userDetails.getUsername(), roles));

	}

}
