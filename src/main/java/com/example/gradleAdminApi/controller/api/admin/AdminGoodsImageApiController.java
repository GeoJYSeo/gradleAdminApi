package com.example.gradleAdminApi.controller.api.admin;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.service.admin.AdminGoodsImageApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value="/api/admin/goodsImage")
public class AdminGoodsImageApiController {

    @Autowired
    private AdminGoodsImageApiLogicService adminGoodsImageApiLogicService;

    @PostMapping("")
    public Header create(Authentication authentication, @RequestParam(value="image", required = false) MultipartFile mpRequest) throws Exception {
        return adminGoodsImageApiLogicService.create(authentication, mpRequest);
    }

    @PutMapping("")
    public Header update(Authentication authentication, @RequestParam(value="image", required = false) MultipartFile mpRequest) throws IOException {
        return adminGoodsImageApiLogicService.update(authentication, mpRequest);
    }

    @DeleteMapping("/{id}")
    public Header delete(Authentication authentication, Long id) throws IOException {
        return adminGoodsImageApiLogicService.delete(authentication, id);
    }
}
