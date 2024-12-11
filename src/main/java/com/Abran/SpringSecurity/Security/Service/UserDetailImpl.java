package com.Abran.SpringSecurity.Security.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Abran.SpringSecurity.Entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailImpl implements UserDetails
{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -2185308353643742018L;
	
	private int id;
	private String email;
	private String password;
	
	private Collection<? extends GrantedAuthority> authoris;
	
	
	public static UserDetailImpl build(user u) {
		List<GrantedAuthority> authorities = u.getRole()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		
		return new UserDetailImpl(u.getId(),u.getEmail(),u.getPassword(), authorities);
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	     return authoris;	
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}
   

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
   
	
	@Override
	  public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	  public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	  public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	  public boolean isEnabled() {
		return true;
	}
	
	
	@Override
	  public boolean equals(Object o) {
		
		if(this==o) {
			
			return true;
		}
		if(o==null||getClass()!=o.getClass()) {
			return false;
		}
		UserDetailImpl user = (UserDetailImpl) o;
		return Objects.equals(id, user.id);
	}
	

}
