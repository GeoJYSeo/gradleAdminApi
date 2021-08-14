package com.example.gradleAdminApi.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsDetailApiResponse;

public interface GoodsApiLogicService {
	
	Header<List<GoodsApiResponse>> index(Pageable pageable, String keyword, String category) throws Exception;
	
	Header<GoodsDetailApiResponse> read(Long id) throws Exception;
}
