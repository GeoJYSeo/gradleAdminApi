package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.model.entity.GoodsImage;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminGoodsImageApiLogicService {

    Header create(Authentication authentication, @RequestParam(value="image", required = false) MultipartFile mpRequest) throws IOException;

    Header update(Authentication authentication, @RequestParam(value="image", required = false) MultipartFile mpRequest) throws IOException;

    Header delete(Authentication authentication, Long id) throws IOException;

    GoodsImageApiResponse response(GoodsImage goodsImage) throws IOException;
}
