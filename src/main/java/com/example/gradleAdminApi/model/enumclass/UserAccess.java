package com.example.gradleAdminApi.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAccess {

    ADMINISTRATOR(9, "Administrator"),
    MANAGER(8, "Manager"),
    MEMBER(0, "Member");

    private final Integer id;
    private final String title;
}
