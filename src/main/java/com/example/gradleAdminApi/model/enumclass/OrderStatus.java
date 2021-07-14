package com.example.gradleAdminApi.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    READY_TO_SHIP(0, "Ready To Ship"),
    SHIPPED(1, "Shipped"),
    CANCELLED(2, "Cancelled");

    private final Integer id;
    private final String title;
}
