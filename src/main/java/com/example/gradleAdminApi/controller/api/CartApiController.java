package com.example.gradleAdminApi.controller.api;

import com.example.gradleAdminApi.controller.CUDController;
import com.example.gradleAdminApi.model.entity.Cart;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.CartApiRequest;
import com.example.gradleAdminApi.model.network.response.CartApiResponse;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.service.CartApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/api/cart")
public class CartApiController extends CUDController<CartApiRequest, CartApiResponse, Cart> {
	
	@Autowired
	private CartApiLogicService cartApiLogicService;

	@Autowired
	private GoodsRepository goodsRepository;
	
	@GetMapping("")
	public Header<List<CartApiResponse>> index(@RequestParam(name = "user-id") Long userId, Authentication authentication) throws Exception {
		log.info("get cart list");
		
		return cartApiLogicService.index(userId, authentication);
	}

	@PostMapping("/stock-check")
	public Header stockCheck(@RequestBody @Valid Header<CartApiRequest> request, Authentication authentication) throws Exception {

        return cartApiLogicService.stockCheck(request, authentication);
	}
}
