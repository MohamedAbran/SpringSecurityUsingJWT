package com.Abran.SpringSecurity.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.Abran.SpringSecurity.Security.Service.UserDetailImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${Abran.SpringSecurity.app.jwtSecret}")
	private String jwtSecret;
	
//	@Value("${Abran.SpringSecurity.app.jwtExpirationMs}")
//	private Integer jwtExpirationMs;
	
	@Value("${jwt.expirationMs}")
	private int jwtExpirationMs;

	
	//100*60*60*24
	
	
	
	public String GenarateToken(Authentication authentication) {
		UserDetailImpl userprincipal = (UserDetailImpl) authentication.getPrincipal();
		
		return Jwts.builder().setSubject((userprincipal.getUsername()))
				 .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationMs)) 
				.signWith(key(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	
	 // Generate refresh token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
                .signWith(key(),SignatureAlgorithm.HS256)
                .compact();
    }
//	
//	public String generateToken(Authentication authentication) {
//        if (authentication == null) {
//            logger.error("Authentication object is null. Cannot generate token.");
//            return null;
//        }
//
//        UserDetailImpl userPrincipal;
//        try {
//            userPrincipal = (UserDetailImpl) authentication.getPrincipal();
//        } catch (ClassCastException e) {
//            logger.error("Unable to cast authentication principal to UserDetailImpl: " + e.getMessage());
//            return null;
//        }
//
//        if (userPrincipal == null) {
//            logger.error("UserPrincipal is null. Cannot generate token.");
//            return null;
//        }
//
//        String username = userPrincipal.getUsername();
//        if (username == null) {
//            logger.error("Username is null. Cannot generate token.");
//            return null;
//        }
//
////        long nowMillis = System.currentTimeMillis();
////        long nowMillis = 3600000;
////        Date now = new Date(nowMillis);
////        String expirationDate = now - jwtExpirationMs ;
//
//        try {
//            return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()+100*60*60*24))
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//        } catch (Exception e) {
//            logger.error("Error generating token: " + e.getMessage());
//            return null;
//        }
//    }
//	
//	
//	
//	
	public String GetUserNameFromJwtToken(String token) {
//		return  Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJwt(token).getBody().getSubject();
		
		  Jws<Claims> jwsClaims = Jwts.parserBuilder()
	                .setSigningKey(key())
	                .build()
	                .parseClaimsJws(token);
              
		  return jwsClaims.getBody().getSubject();
	}
	

//	public Claims GetUserNameFromJwtToken(String token) {
//		return  Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJwt(token).getBody();
//	}
//	
	
	public Key key() {
		//byte [] keybytes = Decoders.BASE64.decode(jwtSecret);
		byte [] keybytes = jwtSecret.getBytes();

		return Keys.hmacShaKeyFor(keybytes);
		//return Keys.secretKeyFor(SignatureAlgorithm.HS256);
		
	}
	
	public boolean ValidateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
			 return true;
		}catch (MalformedJwtException e) {
			logger.error("Invalid Jwt Token:{}"+e.getMessage());
			
		}catch (ExpiredJwtException e) {
			logger.error("Invalid Jwt Token is Expired:{}"+e.getMessage());
			
		}catch (UnsupportedJwtException e) {
			logger.error("Invalid Jwt Token is Unsupport:{}"+e.getMessage());
			
		}catch (IllegalArgumentException e) {
			logger.error("JWT claims String is Empty:{}"+e.getMessage());
			
		}
		return false;
		
		
	}
		

}
