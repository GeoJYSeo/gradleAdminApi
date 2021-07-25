package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.entity.User;

public interface LoginApiLogicService {

	User authenticate(String userId, String passwd) throws Exception ;
}
