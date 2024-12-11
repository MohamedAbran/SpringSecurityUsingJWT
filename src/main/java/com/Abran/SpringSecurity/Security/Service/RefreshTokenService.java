package com.Abran.SpringSecurity.Security.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Abran.SpringSecurity.Entity.RefreshToken;
import com.Abran.SpringSecurity.Entity.user;
import com.Abran.SpringSecurity.Repository.UserRepo;
import com.Abran.SpringSecurity.Service.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepo userRepo;

	public RefreshToken CreateRefreshToken(String username) {
//		 user u = userRepo.findByemail(username).get();
//		 String token  =UUID.randomUUID().toString();
//		 Instant expiry = Instant.now().plusMillis(600000);//10
//		 
//		 RefreshToken refreshToken = new RefreshToken();
//		 refreshToken.setToken(token);
//		 refreshToken.setU(u);
//		 refreshToken.setExpirydate(expiry);

		RefreshToken refreshToken = RefreshToken.builder().u(userRepo.findByemail(username).get())
				.token(UUID.randomUUID().toString()).expirydate(Instant.now().plusMillis(600000)).build();

		return refreshTokenRepository.save(refreshToken);
	}
	
	public Optional<RefreshToken> findByToken(String Token){
		return refreshTokenRepository.findByToken(Token);
	}
	
	public RefreshToken VerifyExpiration(RefreshToken refreshToken) {
		
		if(refreshToken.getExpirydate().compareTo(Instant.now())<0) {
			refreshTokenRepository.delete(refreshToken);
			throw new RuntimeException("Refresh Token Is Expired");
		}
		return refreshToken;
	}
}
