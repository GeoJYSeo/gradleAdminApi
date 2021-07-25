package com.example.gradleAdminApi.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsKeyApiResponse {

    private Long id;

    private String regKey;

    private String goodsName;

    private String status;

    private String regDate;

    private String upDate;

    private UserApiResponse user;
}
