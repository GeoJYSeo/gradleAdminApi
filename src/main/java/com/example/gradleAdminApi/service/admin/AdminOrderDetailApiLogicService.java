package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;

public interface AdminOrderDetailApiLogicService {
	
	OrderDetailApiResponse response(OrderDetail orderDetail);
}
