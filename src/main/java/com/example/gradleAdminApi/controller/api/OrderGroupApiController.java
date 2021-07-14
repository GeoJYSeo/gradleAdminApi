package com.example.gradleAdminApi.controller.api;

import com.example.gradleAdminApi.controller.ICRUDController;
import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.model.entity.OrderGroup;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.OrderGroupApiRequest;
import com.example.gradleAdminApi.model.network.response.OrderGroupApiResponse;
import com.example.gradleAdminApi.repository.OrderDetailRepository;
import com.example.gradleAdminApi.repository.OrderGroupRepository;
import com.example.gradleAdminApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/api/order")
public class OrderGroupApiController extends ICRUDController<OrderGroupApiRequest, OrderGroupApiResponse, OrderGroup> {
}
