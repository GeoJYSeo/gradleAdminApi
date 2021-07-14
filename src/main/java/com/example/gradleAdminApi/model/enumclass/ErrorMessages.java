package com.example.gradleAdminApi.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    WRONG_PASSWORD(1, "Wrong password"),
    EXISTED_EMAIL(2, "Already Existed User Email"),
    NOT_FOUND(3, "Not Found");

    private final Integer id;
    private final String title;
}
