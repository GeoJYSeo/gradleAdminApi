package com.example.gradleAdminApi.controller.api.admin;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.GoodsApiRequest;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsDetailApiResponse;
import com.example.gradleAdminApi.repository.GoodsImageRepository;
import com.example.gradleAdminApi.service.admin.AdminGoodsApiLogicService;
import com.example.gradleAdminApi.utils.HandlingGCPSImages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/api/admin/goods")
public class AdminGoodsApiController {

	@Autowired
	private AdminGoodsApiLogicService adminGoodsApiLogicService;

	@Autowired
	private GoodsImageRepository goodsImageRepository;

	@Autowired
	private HandlingGCPSImages handlingGCPSImages;

	@GetMapping("")
	public Header<List<GoodsApiResponse>> index(
			@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 18) Pageable pageable, Authentication authentication, @RequestParam(value = "keyword", required = false) String keyword) throws Exception {
		return adminGoodsApiLogicService.index(pageable, authentication, keyword);
	}

	@PostMapping("")
	public Header<GoodsApiResponse> create(@RequestPart(value="gdsReq") @Valid Header<GoodsApiRequest> request, @RequestPart(value="images", required = false) List<MultipartFile> mpRequest, Authentication authentication) throws IOException {
		return adminGoodsApiLogicService.create(request, authentication, mpRequest);
	}
	
	@GetMapping("/{id}")
	public Header<GoodsDetailApiResponse> read(@PathVariable Long id, Authentication authentication) throws Exception {
		return adminGoodsApiLogicService.read(id, authentication);
	}	
	
	@PutMapping("")
	public Header<GoodsDetailApiResponse> update(
			@RequestPart(value="gdsReq") Header<GoodsApiRequest> request, Authentication authentication, @RequestPart(value = "images", required = false) List<MultipartFile> mpRequest) throws Exception {
		return adminGoodsApiLogicService.update(request, authentication, mpRequest);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	public Header delete(@PathVariable Long id, Authentication authentication) throws Exception {
		return adminGoodsApiLogicService.delete(id, authentication);
	}
}
