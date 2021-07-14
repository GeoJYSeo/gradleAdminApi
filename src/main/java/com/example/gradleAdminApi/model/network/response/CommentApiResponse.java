package com.example.gradleAdminApi.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentApiResponse {

	private Long id;
	
	private String comment;

	private String commentLabel;

	private String regDate;
	
	private String upDate;
	
	private UserApiResponse userApiResponse;
	
	private GoodsApiResponse goodsApiResponse;
}
