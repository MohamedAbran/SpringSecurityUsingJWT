package com.Abran.SpringSecurity.Security.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Abran.SpringSecurity.Entity.user;
import com.Abran.SpringSecurity.Repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo repo;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	   Optional<user> optional = repo.findByemail(username);
	   
	   if(optional.isPresent()) {
		   user u = optional.get();
		   
		   return UserDetailImpl.build(u);
	   }else {
		    throw new UsernameNotFoundException("User Not Found With User Name::"+username);
	   }
		   
	}

	
}
