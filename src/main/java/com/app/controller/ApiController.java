package com.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.TokenDTO;
import com.app.service.JwtService;

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
	
	@PostMapping("/getName")
	public String check(Authentication auth) {
		return auth.getName();
	}
	
	@PostMapping("/getPrincipal")
	public Object getPrincipal(Authentication auth) {
		return auth.getPrincipal();
	}
	
}
