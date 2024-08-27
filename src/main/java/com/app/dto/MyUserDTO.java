package com.app.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.ToString;

@ToString
public class MyUserDTO implements UserDetails {
	
	UserDTO user;
	List<RoleDTO> roles;

	public MyUserDTO(UserDTO user, List<RoleDTO> roles) {
		this.user = user;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> grant = new HashSet<>();
		roles.forEach(role -> grant.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleNm()))));
		return grant;
	}

	@Override
	public String getPassword() {
		return user.getUserPwd();
	}

	@Override
	public String getUsername() {
		return user.getUserNm();
	}

}
