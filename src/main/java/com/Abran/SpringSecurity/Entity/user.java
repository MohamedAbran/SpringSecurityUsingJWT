package com.Abran.SpringSecurity.Entity;

import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
@Entity
@Table(name = "user" ,uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
public class user
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
   private String email;
   private String password;
   
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<Role> role;
   
   
   public user(String email,String password) {
	   this.email = email;
	   this.password =password;
   }
   
}
