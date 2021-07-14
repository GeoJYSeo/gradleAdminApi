package com.example.gradleAdminApi.service;

import java.util.List;

import com.example.gradleAdminApi.model.network.request.CartApiRequest;
import org.springframework.security.core.Authentication;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.CartApiResponse;

public interface CartApiLogicService {

	Header<List<CartApiResponse>> index(Long id, Authentication authentication) throws Exception;

    Header stockCheck(Header<CartApiRequest> request, Authentication authentication) throws Exception;
}
