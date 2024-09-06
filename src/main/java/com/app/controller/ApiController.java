package com.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.TokenDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;
import com.app.service.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiController {

	@GetMapping("/")
	public String home() {
		return "JwtService Start!!";
	}
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/login")
	public TokenDTO login(@RequestParam Map<String, String> params) {
		return jwtService.login(params);
	}
	
	@PostMapping("/login2")
	public TokenDTO login2(@RequestBody Map<String, String> params) {
		return jwtService.login(params);
	}
	
	@PostMapping("/getName")
	public String check(Authentication auth) {
		return auth.getName();
	}
	
	@PostMapping("/getPrincipal")
	public Object getPrincipal(Authentication auth) {
		return auth.getPrincipal();
	}
	
	@Autowired
	private AuthMapper authMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/sign")
	public Map<String, Object> sign(@RequestParam Map<String, String> paramMap) {
		log.info("신규 회원 : {}", paramMap);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("state", false);
		if(paramMap != null) {
			String userPwd = paramMap.get("userPwd");
			String userNm = paramMap.get("userNm");
			// 사용자 비밀번호 암호화 처리
			if(userPwd != null) {
				userPwd = passwordEncoder.encode(userPwd);
				paramMap.put("userPwd", userPwd);
			}
			// 사용자 정보 DTO로 변경
			UserDTO uDTO = new UserDTO();
			uDTO.setUserNm(userNm);
			uDTO.setUserPwd(userPwd);
			// 사용자 테이블에 등록하기
			int state = authMapper.signup(uDTO);
			if(state == 1) {
				log.info("User : {}", uDTO);
				// 사용자 권한 등록하기
				state = authMapper.roleup(uDTO);
				if(state == 1) {
					// 사용자 토큰 생성
					TokenDTO tokenDTO = jwtService.sign(uDTO);
					if(tokenDTO != null) {
						resultMap.put("state", tokenDTO.isState());
					}
				}
			}
		}
		return resultMap;
	}
	
}
