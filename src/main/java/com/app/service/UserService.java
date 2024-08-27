package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.dto.MyUserDTO;
import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private AuthMapper authMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO user = authMapper.findByUser(username)
				.orElseThrow(() -> new UsernameNotFoundException("유효한 사용자 정보가 아닙니다."));
		List<RoleDTO> roles = authMapper.findByRoles(user);
		return new MyUserDTO(user, roles);
	}
	
}
