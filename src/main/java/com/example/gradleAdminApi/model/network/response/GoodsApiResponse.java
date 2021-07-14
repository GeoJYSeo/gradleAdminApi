package com.example.gradleAdminApi.model.network.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsApiResponse {

	private Long id;
	
	private String gdsName;
	
	private String cateCode;
	
	private String gdsPrice;
	
	private String gdsStock;
	
	private String gdsDesc;
	
	private String regDate;
	
	private String upDate;
	
	private List<GoodsImageApiResponse> goodsImageApiResponseList;
	
	private CategoryApiResponse categoryApiResponse;
}
