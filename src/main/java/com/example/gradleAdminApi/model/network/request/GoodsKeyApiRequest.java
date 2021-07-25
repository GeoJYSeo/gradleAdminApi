package com.example.gradleAdminApi.model.network.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsKeyApiRequest {

    @NonNull
    private Long id;

    @NonNull
    private Long userId;
}
