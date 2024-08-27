package com.app.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.TokenDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;
import com.app.util.JwtToken;

@Service
public class JwtService {
	
	@Autowired
	private AuthMapper authMapper;

	@Autowired
	private JwtToken jwtToken;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public TokenDTO login(Map<String, String> params) {
		String userPwd = params.get("userPwd");
		boolean state = false;
		String jwt = null;
		
		if(userPwd != null) {
			UserDTO user = authMapper.findByUser(params.get("userNm")).orElseThrow();
			
			if( passwordEncoder.matches(userPwd, user.getUserPwd()) ) {
				state = true;
				jwt = jwtToken.setToken(user);
			}
		}
		
		return TokenDTO.builder()
				.state(state)
				.token(jwt)
				.build();
	}
	
}
