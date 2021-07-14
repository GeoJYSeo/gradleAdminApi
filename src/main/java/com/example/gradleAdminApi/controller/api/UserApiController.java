package com.example.gradleAdminApi.controller.api;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.model.network.request.UserApiRequest;
import com.example.gradleAdminApi.model.network.response.UserApiResponse;
import com.example.gradleAdminApi.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value="/api/user")
public class UserApiController {
	
	@Autowired
	private UserApiLogicService userApiLogicService;

	@PostMapping("")
	public Header<UserApiResponse> create(@RequestBody @Valid Header<UserApiRequest> request, Authentication authentication) throws Exception {
		return userApiLogicService.create(request, authentication);
	}

	@GetMapping("/{id}")
	public Header<UserApiResponse> read(@PathVariable Long id, Authentication authentication) throws Exception {
		return userApiLogicService.read(id, authentication);
	}

	@PutMapping("")
	public Header<UserApiResponse> update(@RequestBody Header<UserApiRequest> request, Authentication authentication) throws Exception {
		return userApiLogicService.update(request, authentication);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	public Header delete(@PathVariable Long id, Authentication authentication) throws Exception {
		return userApiLogicService.delete(id, authentication);
	}

	@GetMapping("/email-check")
	public Header emailCheck(@RequestParam("email") String userEmail) throws Exception {
		log.info("post email check");

		return userApiLogicService.emailCheck(userEmail);
	}

	@PostMapping("/passwd-check")
	public Header passwdCheck(@RequestBody @Valid Header<LoginApiRequest> request, Authentication authentication) throws Exception {
		log.info("post password check");

		return userApiLogicService.passwdCheck(request, authentication);
	}
}
