package com.example.gradleAdminApi.controller.api;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.gradleAdminApi.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.model.network.response.LoginApiResponse;
import com.example.gradleAdminApi.service.LoginApiLogicService;
import com.example.gradleAdminApi.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value="/api/login")
public class LoginApiController {
	
	@Autowired
	private LoginApiLogicService loginApiLogicService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/access-token")
	public ResponseEntity<LoginApiResponse> login(@RequestBody @Valid LoginApiRequest request) throws URISyntaxException, Exception {
		log.info("post user login");
		
		String userEmail = request.getUserEmail();
		String passwd = request.getPasswd();
		User user = loginApiLogicService.authenticate(userEmail, passwd);
		
		String accessToken = jwtUtil.createToken(user.getId(), userEmail, user.getAccess().getId());
		
		String url = "access-token";
		return ResponseEntity.created(new URI(url)).body(LoginApiResponse.builder().accessToken(accessToken).build());
	}
}
