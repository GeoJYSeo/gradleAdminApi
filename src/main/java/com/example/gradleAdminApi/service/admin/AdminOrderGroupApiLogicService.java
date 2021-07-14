package com.example.gradleAdminApi.service.admin;

import java.util.List;

import com.example.gradleAdminApi.model.network.request.OrderGroupApiRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.OrderGroupApiResponse;

public interface AdminOrderGroupApiLogicService {

	Header<List<OrderGroupApiResponse>> index(Pageable pageable, Authentication authentication) throws Exception;

	Header<OrderGroupApiResponse> read(Long id, Authentication authentication) throws Exception;
	
	Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> request, Authentication authentication) throws Exception;
	
	@SuppressWarnings("rawtypes")
	Header delete(Long id, Authentication authentication) throws Exception;
}
