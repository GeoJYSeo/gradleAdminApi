package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.GoodsApiRequest;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsDetailApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminGoodsApiLogicService {

	Header<List<GoodsApiResponse>> index(Pageable pageable, Authentication authentication, String keyword) throws Exception;

	Header<GoodsApiResponse> create(Header<GoodsApiRequest> request, Authentication authentication, List<MultipartFile> mpRequest) throws IOException;

	Header<GoodsDetailApiResponse> read(Long id, Authentication authentication) throws Exception;

	Header<GoodsDetailApiResponse> update(Header<GoodsApiRequest> request, Authentication authentication, List<MultipartFile> mpRequest) throws Exception;

	@SuppressWarnings("rawtypes")
	Header delete(Long id, Authentication authentication) throws Exception;
}
