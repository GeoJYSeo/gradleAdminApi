package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.GoodsKeyApiResponse;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;

import java.util.List;

public interface OrderDetailApiLogicService {
	
	OrderDetailApiResponse response(OrderDetail orderDetail, List<GoodsKeyApiResponse> goodsKeyApiResponseList) throws Exception;
}
