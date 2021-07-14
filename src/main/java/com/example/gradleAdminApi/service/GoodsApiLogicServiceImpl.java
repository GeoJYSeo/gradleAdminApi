package com.example.gradleAdminApi.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.Pagination;
import com.example.gradleAdminApi.model.entity.Goods;
import com.example.gradleAdminApi.model.entity.GoodsImage;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsDetailApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.service.admin.AdminCategoryApiLogicService;
import com.example.gradleAdminApi.service.admin.AdminGoodsImageApiLogicServiceImpl;
import com.example.gradleAdminApi.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class GoodsApiLogicServiceImpl implements GoodsApiLogicService{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GoodsRepository goodsRepository;
	
	@Autowired
	private AdminCategoryApiLogicService adminCategoryApiLogicService;
	
	@Autowired
	private AdminGoodsImageApiLogicServiceImpl adminGoodsImageApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(readOnly = true)
	public Header<List<GoodsApiResponse>> index(Pageable pageable, String keyword) throws Exception {
		log.info("get goods list");

		Page<Goods> allGoods = goodsRepository.findByGdsNameContaining(keyword, pageable);

		List<GoodsApiResponse> goodsApiResponseList = allGoods.stream()
				.map(goods -> {
					GoodsApiResponse goodsApiResponse = response(goods);

					// Category
					goodsApiResponse.setCategoryApiResponse(adminCategoryApiLogicService.response(goods.getCategory()));

					// Goods Images
					List<GoodsImage> goodsImageList = goods.getGoodsImageList();
					List<GoodsImageApiResponse> goodsImageApiResponseList = goodsImageList.stream()
							.map(goodsImage -> adminGoodsImageApiLogicService.response(goodsImage))
							.collect(Collectors.toList());
					goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);

					return goodsApiResponse;
				})
				.collect(Collectors.toList());

		Pagination pagination = Pagination.builder()
				.totalPages(allGoods.getTotalPages())
				.totalElement(allGoods.getTotalElements())
				.currentPage(allGoods.getNumber())
				.currentElements(allGoods.getNumberOfElements())
				.build();

		return Header.OK(goodsApiResponseList, pagination);
	}

	@Override
	@Transactional(readOnly = true)
	public Header<GoodsDetailApiResponse> read(Long id) throws Exception {
		log.info("get goods detail");
		
		// Goods
		Goods goods = goodsRepository.findById(id).orElseThrow(NoSuchElementException::new);
		GoodsApiResponse goodsApiResponse = response(goods);

		// Category
		goodsApiResponse.setCategoryApiResponse(adminCategoryApiLogicService.response(goods.getCategory()));

		//Goods Images
		List<GoodsImage> goodsImageList = goods.getGoodsImageList();
		List<GoodsImageApiResponse> goodsImageApiResponseList = goodsImageList.stream()
				.map(goodsImage -> adminGoodsImageApiLogicService.response(goodsImage))
				.collect(Collectors.toList());
		goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);

		GoodsDetailApiResponse goodsDetailApiResponse = GoodsDetailApiResponse.builder()
				.goodsApiResponse(goodsApiResponse)
				.build();

		return Header.OK(goodsDetailApiResponse);
	}

	public GoodsApiResponse response(Goods goods) {

		return GoodsApiResponse.builder()
				.id(goods.getId())
				.gdsName(goods.getGdsName())
				.cateCode(goods.getCateCode())
				.gdsPrice(String.valueOf(goods.getGdsPrice()))
				.gdsStock(String.valueOf(goods.getGdsStock()))
				.gdsDesc(goods.getGdsDesc())
				.regDate(dateFormat.dateFormat(goods.getRegDate()))
				.upDate(dateFormat.dateFormat(goods.getUpDate()))
				.build();
	}


}
