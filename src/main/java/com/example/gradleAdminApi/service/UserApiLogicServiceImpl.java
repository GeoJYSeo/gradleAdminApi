package com.example.gradleAdminApi.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.enumclass.ErrorMessages;
import com.example.gradleAdminApi.model.enumclass.UserAccess;
import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.UserApiRequest;
import com.example.gradleAdminApi.model.network.response.UserApiResponse;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserApiLogicServiceImpl implements UserApiLogicService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<UserApiResponse> create(Header<UserApiRequest> request, Authentication authentication) throws Exception {
		log.info("post user register");
		
		UserApiRequest userApiRequest = request.getData();

		String encodedPassword = passwordEncoder.encode(userApiRequest.getPasswd());
		
		User user = User.builder()
				.userEmail(userApiRequest.getUserEmail())
				.passwd(encodedPassword)
				.userName(userApiRequest.getUserName())
				.userSurname(userApiRequest.getUserSurname())
				.birthday(userApiRequest.getBirthday())
				.postCode(userApiRequest.getPostCode())
				.userAddr1(userApiRequest.getUserAddr1())
				.userAddr2(userApiRequest.getUserAddr2())
				.userAddr3(userApiRequest.getUserAddr3())
				.phoneNum(userApiRequest.getPhoneNum())
				.access(UserAccess.MEMBER)
				.upDate(LocalDateTime.now())
				.build();
		
		User newUser = userRepository.save(user);
		
		return Header.OK(response(newUser));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Header<UserApiResponse> read(Long id, Authentication authentication) throws Exception {
		log.info("get user detail");

		jwtUtil.getAuthPermission(id, authentication);
		return Header.OK(response(userRepository.findById(id).orElseThrow(NoSuchElementException::new)));
	}
	
	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<UserApiResponse> update(Header<UserApiRequest> request, Authentication authentication) throws Exception {
		log.info("put user modify");

		jwtUtil.getAuthPermission(request.getData().getId(), authentication);
		UserApiRequest userApiRequest = request.getData();

		User selectedUser = userRepository.findById(userApiRequest.getId()).orElseThrow(NoSuchElementException::new);

		if(userApiRequest.getPasswd() != null) {
			String encodedPassword = passwordEncoder.encode(userApiRequest.getPasswd());

			selectedUser.setPasswd(encodedPassword);
		} else {
			selectedUser.setUserName(userApiRequest.getUserName())
					.setUserSurname(userApiRequest.getUserSurname())
					.setPostCode(userApiRequest.getPostCode())
					.setUserAddr1(userApiRequest.getUserAddr1())
					.setUserAddr2(userApiRequest.getUserAddr2())
					.setUserAddr3(userApiRequest.getUserAddr3())
					.setPhoneNum(userApiRequest.getPhoneNum())
					.setUpDate(LocalDateTime.now());
		}

		return Header.OK(response(userRepository.save(selectedUser)));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {NoSuchElementException.class, Exception.class})
	public Header delete(Long id, Authentication authentication) throws Exception {
		log.info("delete user");

		jwtUtil.getAuthPermission(id, authentication);
		userRepository.delete(userRepository.findById(id).orElseThrow(NoSuchElementException::new));

		return Header.OK();
	}

	@Override
	@Transactional(readOnly = true)
	public Header emailCheck(String userEmail) throws Exception {
		log.info("email check");

		Optional<User> hasEmail = userRepository.findByUserEmail(userEmail);

		return hasEmail.isEmpty() ? Header.OK() : Header.ERROR(ErrorMessages.EXISTED_EMAIL.getTitle());
	}

	@Override
	@Transactional(readOnly = true)
	public Header passwdCheck(Header<LoginApiRequest> request, Authentication authentication) throws Exception {
		log.info("password check");

		jwtUtil.getAuthPermission(request.getData().getId(), authentication);
		User user = userRepository.findById(request.getData().getId()).orElseThrow(NoSuchElementException::new);
		return passwordEncoder.matches(request.getData().getPasswd(), user.getPasswd()) ? Header.OK() : Header.ERROR(ErrorMessages.WRONG_PASSWORD.getTitle());
	}
	
	public UserApiResponse response(User user) {

//		String access = user.getAccess() == 9 ? "Administrator" : user.getAccess() == 8 ? "Manager" : "Member";

		return UserApiResponse.builder()
				.id(user.getId())
				.userEmail(user.getUserEmail())
				.userName(user.getUserName())
				.userSurname(user.getUserSurname())
				.birthday(user.getBirthday())
				.postCode(user.getPostCode())
				.userAddr1(user.getUserAddr1())
				.userAddr2(user.getUserAddr2())
				.userAddr3(user.getUserAddr3())
				.phoneNum(user.getPhoneNum())
				.access(user.getAccess().getId())
				.strAccess(user.getAccess().getTitle())
				.lastLoginAt(dateFormat.dateFormat(user.getLastLoginAt()))
				.regDate(dateFormat.dateFormat(user.getRegDate()))
				.upDate(dateFormat.dateFormat(user.getUpDate()))
				.build();
	}

}
