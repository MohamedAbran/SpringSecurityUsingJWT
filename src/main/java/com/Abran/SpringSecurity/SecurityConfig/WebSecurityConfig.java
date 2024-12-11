package com.Abran.SpringSecurity.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Abran.SpringSecurity.Jwt.AuthEntryPointJwt;
import com.Abran.SpringSecurity.Jwt.AuthTokenFilter;
import com.Abran.SpringSecurity.Security.Service.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private UserDetailsServiceImpl 	userDetailsService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	
	
	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider  =new DaoAuthenticationProvider();
		
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(csrf->csrf.disable())
		.exceptionHandling(exception ->exception.authenticationEntryPoint(unauthorizedHandler))
		.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//SessionCreationPolicy.STATELESS -->this used everytime new session is created
		.authorizeHttpRequests(auth ->auth.requestMatchers("/api/test/**","api/test/user").permitAll()
				.requestMatchers("api/auth/**").permitAll()
				.anyRequest().authenticated());
		
		
		
		http.authenticationProvider(authenticationProvider());
		
		http.addFilterBefore(authTokenFilter(),UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		 
	}
	
	
}
