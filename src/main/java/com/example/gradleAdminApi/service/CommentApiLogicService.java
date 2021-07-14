package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.CommentApiResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentApiLogicService {

    Header<List<CommentApiResponse>> readWithGoodsId(Long id) throws Exception;

    Header<List<CommentApiResponse>> readWithUserId(Long id, Authentication authentication) throws Exception;
}
