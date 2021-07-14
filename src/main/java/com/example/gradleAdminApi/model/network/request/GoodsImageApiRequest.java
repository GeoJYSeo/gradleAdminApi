package com.example.gradleAdminApi.model.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsImageApiRequest {
	
	private Long id;

	private String imgName;

	private String oriName;

	private String gdsImg;

	private String gdsThumbImg;

	private Long imgSize;
	
	private int imgFlg;
	
	private String regDate;
	
	private String upDate;
	
	private Long goodsId;
}
