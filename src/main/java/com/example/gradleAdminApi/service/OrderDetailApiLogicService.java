package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;

public interface OrderDetailApiLogicService {
	
	OrderDetailApiResponse response(OrderDetail orderDetail) throws Exception;
}
