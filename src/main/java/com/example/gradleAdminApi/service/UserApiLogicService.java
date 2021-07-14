package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.model.network.request.UserApiRequest;
import org.springframework.security.core.Authentication;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.UserApiResponse;

public interface UserApiLogicService {

	Header<UserApiResponse> create(Header<UserApiRequest> request, Authentication authentication) throws Exception;
	
	Header<UserApiResponse> read(Long id, Authentication authentication) throws Exception;

	Header<UserApiResponse> update(Header<UserApiRequest> request, Authentication authentication) throws Exception;

	Header delete(Long id, Authentication authentication) throws Exception;

    Header emailCheck(String userEmail) throws Exception;

	Header passwdCheck(Header<LoginApiRequest> request, Authentication authentication) throws Exception;
}
