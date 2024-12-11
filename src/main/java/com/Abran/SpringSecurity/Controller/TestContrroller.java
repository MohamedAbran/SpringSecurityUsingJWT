package com.Abran.SpringSecurity.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestContrroller {
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String user() {
		return "User Board";
	}
	
	

}
