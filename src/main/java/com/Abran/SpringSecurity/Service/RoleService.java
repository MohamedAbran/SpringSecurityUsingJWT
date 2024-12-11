package com.Abran.SpringSecurity.Service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Abran.SpringSecurity.Entity.ERole;
import com.Abran.SpringSecurity.Entity.Role;
import com.Abran.SpringSecurity.Repository.RoleRepo;

import jakarta.annotation.PostConstruct;

@Service
public class RoleService {

	@Autowired
	private RoleRepo roleRepo;
	
	@PostConstruct // This annotation makes the method run after the bean is created (i.e., at application startup)
	public void InitializeRole() {
		if(roleRepo.count()==0) {
			Role userrole = new Role(ERole.ROLE_USER);
			Role adminrole = new Role(ERole.ROLE_ADMIN);
			Role modrole = new Role(ERole.ROLE_MODERATOR);
			
			roleRepo.saveAll(Arrays.asList(userrole,adminrole,modrole));
			
			System.out.println("Role Inserted SucceesFully");
		}else {
			System.out.println("Role Already exists data Base");
		}
	}
}
