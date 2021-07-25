package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.GoodsKeyApiResponse;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;

import java.util.List;

public interface AdminOrderDetailApiLogicService {
	
	OrderDetailApiResponse response(OrderDetail orderDetail, List<GoodsKeyApiResponse> goodsKeyApiResponseList);
}
