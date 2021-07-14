package com.example.gradleAdminApi.controller.api.admin;

import java.util.List;

import com.example.gradleAdminApi.model.network.request.OrderGroupApiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.OrderGroupApiResponse;
import com.example.gradleAdminApi.service.admin.AdminOrderGroupApiLogicService;

@RestController
@RequestMapping(value="/api/admin/order")
public class AdminOrderGroupApiController {

	@Autowired
	private AdminOrderGroupApiLogicService adminOrderApiLogicService;
	
	@GetMapping("")
	public Header<List<OrderGroupApiResponse>> index(Pageable pageable, Authentication authentication) throws Exception {
		return adminOrderApiLogicService.index(pageable, authentication);
	}
	
	@GetMapping("/{id}")
	public Header<OrderGroupApiResponse> read(@PathVariable Long id, Authentication authentication) throws Exception {
		return adminOrderApiLogicService.read(id, authentication);
	}
	
	@PutMapping("")
	public Header<OrderGroupApiResponse> update(@RequestBody Header<OrderGroupApiRequest> request, Authentication authentication) throws Exception {
		System.out.println(request);
		return adminOrderApiLogicService.update(request, authentication);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	public Header delete(@PathVariable Long id, Authentication authentication) throws Exception {
		return adminOrderApiLogicService.delete(id, authentication);
	}
}
