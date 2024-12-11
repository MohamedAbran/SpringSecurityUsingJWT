package com.Abran.SpringSecurity.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Abran.SpringSecurity.Entity.user;

@Repository
public interface UserRepo extends JpaRepository<user,Integer> {
	
     Optional<user> findByemail(String email);
     
     boolean existsByemail(String email);

}
