package com.example.gradleAdminApi.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryApiResponse {

	private Long id;
	
	private String cateName;
	
	private String cateCode;
	
	private String cateCodeRef;
	
	private String regDate;
	
//	private List<Goods> goodsList;
}
