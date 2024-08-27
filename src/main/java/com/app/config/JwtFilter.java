package com.app.config;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.dto.UserDTO;
import com.app.service.UserService;
import com.app.util.JwtToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtToken jwtToken;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 접속자 대상 IP 확인
		Function<HttpServletRequest, String> getClientIP = req -> req.getHeader("X-Forwarded-For") == null ? req.getRemoteAddr() : req.getHeader("X-Forwarded-For");
		// 토큰 여부 확인
		Function<HttpServletRequest, String> getAuthorization = req -> req.getHeader("Authorization");
		// 토큰에서 사용자 여부 확인
		Function<UserDTO, String> getUserName = user -> (user != null) ? user.getUserNm() : null;
		// 사용자 식별자 여부 확인
		Consumer<String> setAuthentication = userNm -> {
			if(userNm != null) {
				UserDetails userDetails = userService.loadUserByUsername(userNm);
				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities()
						));
			}
		};
		
		
		log.info("========================JwtFilter=============================");
		log.info("Client IP : {}", getClientIP.apply(request));
		log.info("Request METHOD : {}", request.getMethod());
		log.info("Request HOST : {}", request.getServerName());
		log.info("Request PORT : {}", request.getServerPort());
		log.info("Request URI : {}", request.getRequestURI());
		String  authorization = getAuthorization.apply(request);
		if(authorization != null && !authorization.isEmpty()) {
			if(jwtToken.isValidToken(authorization)) {
				setAuthentication.accept(getUserName.apply(jwtToken.getUser(authorization)));
			}
		}
		log.info("========================JwtFilter=============================");
		
		
		// 요청 받은 해당 메소드 전달
		filterChain.doFilter(request, response);
	}

}
