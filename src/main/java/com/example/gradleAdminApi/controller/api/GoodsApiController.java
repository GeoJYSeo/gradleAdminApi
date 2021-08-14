package com.example.gradleAdminApi.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsDetailApiResponse;
import com.example.gradleAdminApi.service.GoodsApiLogicService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/api/goods")
public class GoodsApiController {
	
	@Autowired
	private GoodsApiLogicService goodsApiLogicService;
	
	@GetMapping("")
	public Header<List< GoodsApiResponse>> index(
			@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size=18) Pageable pageable, @RequestParam String keyword, @RequestParam String categoryName) throws Exception {
		log.info("get goods list");

		return goodsApiLogicService.index(pageable, keyword, categoryName);
	}
	
	@GetMapping("/{id}")
	public Header<GoodsDetailApiResponse> read(@PathVariable Long id) throws Exception {
		log.info("get goods detail");
		
		return goodsApiLogicService.read(id);
	}
}
