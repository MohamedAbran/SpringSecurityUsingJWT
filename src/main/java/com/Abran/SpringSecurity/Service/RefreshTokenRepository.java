package com.Abran.SpringSecurity.Service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Abran.SpringSecurity.Entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Optional<RefreshToken> findByToken(String token);

}
