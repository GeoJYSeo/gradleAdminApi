package com.example.gradleAdminApi.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.gradleAdminApi.exception.UnauthenticatedException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.gradleAdminApi.utils.JwtUtil;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private JwtUtil jwtUtil;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
	}
	
	// JWT 분석
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, NumberFormatException, NullPointerException {

		Authentication authentication = getAuthentication(request);
		if(authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);		// chain 을 통해서 다음 작업을 계속해서 연결
	}
	
	private Authentication getAuthentication(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");

		if(token == null) {
			return null;
		}

		//	JWT 분석 작업
		Claims claims = jwtUtil.getClaims(token.substring("Bearer ".length()));
		Authentication authentication = new UsernamePasswordAuthenticationToken(claims , null);

		return authentication;
	}
}
