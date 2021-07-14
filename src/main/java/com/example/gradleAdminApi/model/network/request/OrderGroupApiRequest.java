package com.example.gradleAdminApi.model.network.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderGroupApiRequest {

	private Long id;

	@NotBlank
	private String userName;

	@NotBlank
	private String userSurname;

	@Size(max = 7, min = 7)
	private String postCode;

	@NotBlank
	private String userAddr1;

	@NotBlank
	private String userAddr2;

	@NotBlank
	private String userAddr3;

	@Size(max = 11, min = 11)
	@NotBlank
	private String phoneNum;

	@NotNull
	private BigDecimal totalPrice;

	@NotNull
	private int totalQuantity;

	@NotBlank
	private String paymentType;

	@NotNull
	private int orderStatus;

	@NotNull
	private Long userId;

	@NotEmpty
	private List<Long> goodsIds;

	@NotNull
	private Boolean isDirectOrder;
}
