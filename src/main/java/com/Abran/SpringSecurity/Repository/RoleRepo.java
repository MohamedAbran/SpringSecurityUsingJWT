package com.Abran.SpringSecurity.Repository;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Abran.SpringSecurity.Entity.ERole;
import com.Abran.SpringSecurity.Entity.Role;

public interface RoleRepo extends JpaRepository<Role,Integer> {
     
	Optional<Role> findByname(ERole name);
}
