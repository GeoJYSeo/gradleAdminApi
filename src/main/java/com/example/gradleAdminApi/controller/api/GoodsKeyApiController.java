package com.example.gradleAdminApi.controller.api;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.GoodsKeyApiRequest;
import com.example.gradleAdminApi.service.GoodsKeyApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value="/api/goods-key")
public class GoodsKeyApiController {

    @Autowired
    private GoodsKeyApiLogicService goodsKeyApiLogicService;

    @PutMapping("")
    public void update(@RequestBody @Valid Header<GoodsKeyApiRequest> request, Authentication authentication) throws Exception {
        goodsKeyApiLogicService.update(request, authentication);
    }
}
