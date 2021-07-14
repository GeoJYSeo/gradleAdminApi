package com.example.gradleAdminApi.model.network.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderGroupApiResponse {

	private Long id;

	private String orderNum;
	
	private String orderRcp;
	
	private String postCode;
	
	private String userAddr1;
	
	private String userAddr2;
	
	private String userAddr3;
	
	private String phoneNum;
	
	private BigDecimal totalPrice;
	
	private int totalQuantity;
	
	private String paymentType;
	
	private String orderStatus;
	
	private String orderDate;

	private String upDate;
	
	private UserApiResponse userApiResponse;
	
	private List<OrderDetailApiResponse> orderDetailApiResponseList;
}
