package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.model.entity.GoodsKey;
import com.example.gradleAdminApi.model.enumclass.OrderStatus;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.GoodsKeyApiRequest;
import com.example.gradleAdminApi.model.network.response.GoodsKeyApiResponse;
import com.example.gradleAdminApi.repository.GoodsKeyRepository;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsKeyApiLogicServiceImpl implements GoodsKeyApiLogicService {

    @Autowired
    private GoodsKeyRepository goodsKeyRepository;

    @Autowired
    private UserApiLogicServiceImpl userApiLogicService;

    @Autowired
    private DateFormat dateFormat;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void update(Header<GoodsKeyApiRequest> request, Authentication authentication) {

        jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
        GoodsKey goodsKey = goodsKeyRepository.findById(request.getData().getId()).orElseThrow(NoSuchElementException::new);
        goodsKeyRepository.save(goodsKey.setStatus(OrderStatus.REVEALED));
    }

    public GoodsKeyApiResponse response(GoodsKey goodsKey, boolean isAdmin) {

        List<String> statusList = new ArrayList<>();
        statusList.add("REVEALED");
        statusList.add("CANCELLED");

        return GoodsKeyApiResponse.builder()
                .id(goodsKey.getId())
                .regKey(goodsKey.getRegKey())
                .goodsName(goodsKey.getGoodsName())
                .status(statusList.contains(goodsKey.getStatus().getTitle()) || isAdmin ? goodsKey.getStatus().getTitle() : null)
                .user(userApiLogicService.response(goodsKey.getUser()))
                .regDate(dateFormat.dateFormat(goodsKey.getRegDate()))
                .upDate(dateFormat.dateFormat(goodsKey.getUpDate()))
                .build();
    }
}
