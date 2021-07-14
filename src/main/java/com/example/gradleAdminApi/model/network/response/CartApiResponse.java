package com.example.gradleAdminApi.model.network.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartApiResponse {
	
	private Long id;
	
	private int cartQuantity;
	
	private BigDecimal cartPrice;
	
	private String regDate;

	private String upDate;
	
	private UserApiResponse userApiResponse;
	
	private GoodsApiResponse goodsApiResponse;
}
