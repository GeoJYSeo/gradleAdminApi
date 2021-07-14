package com.example.gradleAdminApi.model.network.request;

import java.math.BigDecimal;
import java.util.List;

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
public class GoodsApiRequest {

	private Long id;

	@NotBlank
	private String gdsName;

	@NotBlank
	private String cateCode;

	@NotNull
	private BigDecimal gdsPrice;

	@NotNull
	private int gdsStock;

	@NotBlank
	private String gdsDesc;

	private List<Long> delImageIds;
}
