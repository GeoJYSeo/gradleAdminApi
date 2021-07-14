package com.example.gradleAdminApi.utils;

import java.security.Key;
import java.util.*;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.repository.UserRepository;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Slf4j
public class JwtUtil {

	@Autowired
	private UserRepository userRepository;
	
    private Key key;

    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
	
	public String createToken(Long userId, String userEmail, int access) {

		long curTime = System.currentTimeMillis();
		
		return Jwts.builder()
				.claim("userId", userId)
				.claim("userEmail", userEmail)
				.claim("access", access)
				.setExpiration(new Date(curTime + 60 * 60 * 24 * 1000))
				.setIssuedAt(new Date(curTime))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims getClaims(String token) {

    	try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (Exception e) {
    		log.error("JWT Token ERROR: {}", e.getMessage());
    		return null;
		}
	}
	
	public Map<String, Object> getClaimsData(Authentication authentication) {
		
		Claims claims = null;
		if (authentication != null) {
			claims = (Claims) authentication.getPrincipal();			
		} else {			
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("userEmail", claims.get("userEmail", String.class));
		map.put("access", claims.get("access", Integer.class));
		
		return map;
	}

	public void getAuthPermission(Long id, Authentication authentication) {

    	if(authentication == null) {
    		throw new UnauthenticatedException();
		}
    	String userEmail = userRepository.findById(id).orElseThrow(NoSuchElementException::new).getUserEmail();

    	if(!userEmail.equals(getClaimsData(authentication).get("userEmail").toString())) {
    		throw new UnauthenticatedException();
		}
	}

	public void getAccessAllPermission(Authentication authentication) {
    	if(authentication == null || !List.of(8,9).contains(getClaimsData(authentication).get("access"))) {
			throw new UnauthenticatedException();
		};
	}

	public void getAccessAdminPermission(Authentication authentication) {
    	if(!Objects.equals(9, getClaimsData(authentication).get("access"))) {
    		throw new UnauthenticatedException();
		}
	}
}
