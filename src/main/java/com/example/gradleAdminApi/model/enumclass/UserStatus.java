package com.example.gradleAdminApi.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    ACTIVATED(0, "ACTIVATED"),
    DELETED_PENDING(1, "DELETED_PENDING"),
    DELETED(2, "DELETED");

    private final Integer id;
    private final String title;
}
