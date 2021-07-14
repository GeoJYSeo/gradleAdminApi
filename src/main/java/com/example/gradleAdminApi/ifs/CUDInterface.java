package com.example.gradleAdminApi.ifs;

import org.springframework.security.core.Authentication;

import com.example.gradleAdminApi.model.network.Header;

public interface CUDInterface<Req, Res> {
	
	public Header<Res> create(Header<Req> request, Authentication authentication) throws Exception;
	
	public Header<Res> update(Header<Req> request, Authentication authentication) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public Header delete(Long id, Long userId, Authentication authentication) throws Exception;
}
