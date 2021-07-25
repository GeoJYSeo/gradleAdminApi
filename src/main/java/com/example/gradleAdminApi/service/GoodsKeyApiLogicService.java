package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.GoodsKeyApiRequest;
import org.springframework.security.core.Authentication;

public interface GoodsKeyApiLogicService {

    void update(Header<GoodsKeyApiRequest> request, Authentication authentication);
}
