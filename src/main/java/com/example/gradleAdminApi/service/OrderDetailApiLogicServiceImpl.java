package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.entity.GoodsKey;
import com.example.gradleAdminApi.model.network.response.GoodsKeyApiResponse;
import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderDetailApiLogicServiceImpl implements OrderDetailApiLogicService {

	@Autowired
	private GoodsApiLogicServiceImpl goodsApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	public OrderDetailApiResponse response(OrderDetail orderDetail, List<GoodsKeyApiResponse> goodsKeyApiResponseList) {

		return OrderDetailApiResponse.builder()
				.id(orderDetail.getId())
				.goodsQuantity(orderDetail.getGoodsQuantity())
				.goodsTotalPrice(orderDetail.getGoodsTotalPrice())
				.regDate(dateFormat.dateFormat(orderDetail.getRegDate()))
				.goodsApiResponse(goodsApiLogicService.response(orderDetail.getGoods()))
				.goodsKeyApiResponseList(goodsKeyApiResponseList)
				.build();
	}
}
