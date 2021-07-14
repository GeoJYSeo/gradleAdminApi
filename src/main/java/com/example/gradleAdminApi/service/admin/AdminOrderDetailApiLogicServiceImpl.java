package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;
import com.example.gradleAdminApi.service.GoodsApiLogicServiceImpl;

@Service
public class AdminOrderDetailApiLogicServiceImpl implements AdminOrderDetailApiLogicService{
	
	@Autowired
	private GoodsApiLogicServiceImpl goodsApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	public OrderDetailApiResponse response(OrderDetail orderDetail) {

		return OrderDetailApiResponse.builder()
				.id(orderDetail.getId())
				.goodsQuantity(orderDetail.getGoodsQuantity())
				.regDate(dateFormat.dateFormat(orderDetail.getRegDate()))
				.goodsApiResponse(goodsApiLogicService.response(orderDetail.getGoods()))
				.build();
	}
}
