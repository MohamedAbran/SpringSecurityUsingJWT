package com.Abran.SpringSecurity.Jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Abran.SpringSecurity.Security.Service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl detailsServiceImpl;
	
	
	private String parseJwt(HttpServletRequest request) {
		
		String headAuth =request.getHeader("Authorization");
		
		if(StringUtils.hasText(headAuth)&& headAuth.startsWith("Bearer ")) {
			return headAuth.substring(7);
		}
		return null;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String jwt  =parseJwt(request);
			if(jwt!=null && jwtUtils.ValidateJwtToken(jwt) ) {
				
				String email =jwtUtils.GetUserNameFromJwtToken(jwt);
				
				UserDetails userdetails  = detailsServiceImpl.loadUserByUsername(email);
				
				UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(userdetails,null,userdetails.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Cannot Set User Authentication::",e);
		}
		
		filterChain.doFilter(request, response);
	
		
	}

}
