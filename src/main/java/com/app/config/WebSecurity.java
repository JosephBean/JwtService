package com.app.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurity {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 스프링 필터 비활성화
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.logout(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
//		http.cors(AbstractHttpConfigurer::disable);
		// 해당 IP 또는 DNS 접속 허용 추가
		http.cors(cors -> cors.configurationSource(req -> corsOrigin()));
		// 스프링쪽 자동 생성 되는 session 비활성화
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// JWT 토큰 확인를 위한 필터 추가
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		// 접근 URL 정의 (권한 설정)
		http.authorizeHttpRequests(req -> {
			req.requestMatchers("/**").permitAll(); // 해당 URL 접근 허용
//			req.anyRequest().authenticated(); // 나머지 URL 접근 막기
		});
		return http.build();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	private CorsConfiguration corsOrigin() {
		CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500")); // 허용할 도메인
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH")); // 허용할 메서드
        corsConfig.setAllowedHeaders(Arrays.asList("*")); // 허용할 헤더
        corsConfig.setAllowCredentials(true); // 자격 증명 허용
        return corsConfig;
	}
	
}
