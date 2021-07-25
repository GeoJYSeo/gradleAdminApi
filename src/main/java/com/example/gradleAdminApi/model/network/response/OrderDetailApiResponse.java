package com.example.gradleAdminApi.model.network.response;

import java.math.BigDecimal;
import java.util.List;

import com.example.gradleAdminApi.model.entity.GoodsKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailApiResponse {

	private Long id;

	private int goodsQuantity;

	private BigDecimal goodsTotalPrice;

	private String regDate;

	private GoodsApiResponse goodsApiResponse;

	private List<GoodsKeyApiResponse> goodsKeyApiResponseList;
}
