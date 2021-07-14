package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;

@Service
public class OrderDetailApiLogicServiceImpl implements OrderDetailApiLogicService {

	@Autowired
	private GoodsApiLogicServiceImpl goodsApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	public OrderDetailApiResponse response(OrderDetail orderDetail) {

		return OrderDetailApiResponse.builder()
				.id(orderDetail.getId())
				.goodsQuantity(orderDetail.getGoodsQuantity())
				.goodsTotalPrice(orderDetail.getGoodsTotalPrice())
				.regDate(dateFormat.dateFormat(orderDetail.getRegDate()))
				.goodsApiResponse(goodsApiLogicService.response(orderDetail.getGoods()))
				.build();
	}
}
