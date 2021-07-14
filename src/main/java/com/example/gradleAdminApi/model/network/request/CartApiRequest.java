package com.example.gradleAdminApi.model.network.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartApiRequest {

	private Long id;

	@NotNull
	private int cartQuantity;

	@NotNull
	private Long userId;

	@NotEmpty
	private List<Long> goodsIds;
}
