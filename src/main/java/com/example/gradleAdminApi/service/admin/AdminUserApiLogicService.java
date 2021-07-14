package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.LoginApiRequest;
import com.example.gradleAdminApi.model.network.request.UserApiRequest;
import com.example.gradleAdminApi.model.network.response.UserApiResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdminUserApiLogicService {

	Header<List<UserApiResponse>> index(Authentication authentication, int searchKind, String keyword) throws Exception;

	Header<UserApiResponse> create(Header<UserApiRequest> request, Authentication authentication) throws Exception;

	Header<UserApiResponse> read(Long id, Authentication authentication) throws Exception;

	Header<UserApiResponse> update(Header<UserApiRequest> request, Authentication authentication) throws Exception;

	@SuppressWarnings("rawtypes")
	Header delete(Long id, Authentication authentication) throws Exception;

    Header passwdCheck(Header<LoginApiRequest> request, Authentication authentication) throws Exception;
}
