package com.example.gradleAdminApi.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsImageApiResponse {

	private Long id;

	private URL imgName;
	
	private String oriName;
	
	private String gdsImg;
	
	private String gdsThumbImg;
	
	private Long imgSize;
	
	private int imgFlg;
	
	private String regDate;
	
	private String upDate;
	
	private Long goodsId;
}
