package com.example.gradleAdminApi.ifs;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.example.gradleAdminApi.model.network.Header;

public interface ICRUDInterface<Req, Res> {
	
	public Header<List<Res>> index(Long id, Pageable pageable, Authentication authentication) throws Exception;
	
	public Header<Res> create(Header<Req> request, Authentication authentication) throws Exception;
	
	public Header<Res> read(Long id, Long userId, Authentication authentication) throws Exception;
	
	public Header<Res> update(Header<Req> request, Authentication authentication) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public Header delete(Long id, Authentication authentication) throws Exception;
}
