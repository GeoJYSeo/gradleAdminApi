package com.example.gradleAdminApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.exception.LoginException;
import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class LoginApiLogicServiceImpl implements LoginApiLogicService {

	@Autowired
    private UserRepository userRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public User authenticate(String userEmail, String passwd) throws Exception {
		log.info("post user authenticate");
		
		User user = userRepository.findByUserEmail(userEmail).orElseThrow(LoginException::new);

		if(!passwordEncoder.matches(passwd, user.getPasswd())) {
			throw new LoginException();
		}

		userRepository.save(user.setLastLoginAt(LocalDateTime.now()));

		return user;
	}
}
