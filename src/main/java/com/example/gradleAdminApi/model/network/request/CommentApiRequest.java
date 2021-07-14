package com.example.gradleAdminApi.model.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentApiRequest {

	private Long id;

	@NotBlank
	private String comment;

	@NotNull
	private Long userId;

	@NotNull
	private Long goodsId;
}
