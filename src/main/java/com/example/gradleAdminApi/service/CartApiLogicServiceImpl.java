package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.exception.EmailNotExistedException;
import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.entity.Cart;
import com.example.gradleAdminApi.model.entity.Goods;
import com.example.gradleAdminApi.model.entity.GoodsImage;
import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.model.enumclass.ErrorMessages;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.CartApiRequest;
import com.example.gradleAdminApi.model.network.response.CartApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import com.example.gradleAdminApi.repository.CartRepository;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.service.admin.AdminGoodsImageApiLogicServiceImpl;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CartApiLogicServiceImpl extends BaseService<CartApiRequest, CartApiResponse, Cart> implements CartApiLogicService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserApiLogicServiceImpl userApiLogicService;
	
	@Autowired
	private GoodsApiLogicServiceImpl goodsApiLogicService;

	@Autowired
	private AdminGoodsImageApiLogicServiceImpl adminGoodsImageApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(readOnly = true)
	public Header<List<CartApiResponse>> index(Long userId, Authentication authentication) throws Exception {
		log.info("get cart list");

		jwtUtil.getAuthPermission(userId, authentication);
		List<Cart> allCartItems = baseRepository.findAll();

		List<CartApiResponse> cartApiResponse = allCartItems.stream()
				.map(this::response)
				.collect(Collectors.toList());

		return Header.OK(cartApiResponse);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CartApiResponse> create(Header<CartApiRequest> request, Authentication authentication) throws Exception {
		log.info("post cart register");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		CartApiRequest cartApiRequest = request.getData();

		Optional<Goods> goods = goodsRepository.findById(cartApiRequest.getGoodsIds().get(0));

		String userEmail = jwtUtil.getClaimsData(authentication).get("userEmail").toString();

		Optional<User> user = userRepository.findByUserEmail(userEmail);

		if(goods.isEmpty() || user.isEmpty()) {
			return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
		}

		Optional<Cart> filteredCart = cartRepository.findByUserIdAndGoodsId(user.get().getId(), goods.get().getId());

		Cart cart = Cart.builder()
				.user(user.get())
				.goods(goods.get())
				.build();

		if(filteredCart.isEmpty()) {
			cart.setCartQuantity(request.getData().getCartQuantity());
		} else {
			cart.setId(filteredCart.get().getId());
			cart.setCartQuantity(filteredCart.get().getCartQuantity() + request.getData().getCartQuantity());
			cart.setRegDate(filteredCart.get().getRegDate());
		}

		cart.setCartPrice(goods.get().getGdsPrice().multiply(new BigDecimal(cart.getCartQuantity())));

		return Header.OK(response(baseRepository.save(cart)));
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CartApiResponse> update(Header<CartApiRequest> request, Authentication authentication) throws Exception {
		log.info("put cart modify");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		CartApiRequest cartApiRequest = request.getData();

		Optional<Goods> goods = goodsRepository.findById(cartApiRequest.getGoodsIds().get(0));
		Optional<Cart> cart = baseRepository.findById(cartApiRequest.getId());

		if(goods.isPresent() && cart.isPresent()) {
			cart.get().setCartPrice(goods.get().getGdsPrice().multiply(new BigDecimal(cartApiRequest.getCartQuantity())))
					.setCartQuantity(cartApiRequest.getCartQuantity());
			return Header.OK(response(baseRepository.save(cart.get())));
		}
		return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header delete(Long id, Long userId, Authentication authentication) throws Exception {
		log.info("delete user cart");

		jwtUtil.getAuthPermission(userId, authentication);
//		cartRepository.deleteAllByIdIn(cartGoodsList.stream().map(Cart::getId).collect(Collectors.toList()));
		Optional<Cart> cart = getCart(id);

		if(cart.isPresent()) {
			baseRepository.delete(cart.get());
			return Header.OK();
		}
		return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
	}

	@Override
	public Header stockCheck(Header<CartApiRequest> request, Authentication authentication) throws Exception {
		log.info("post stock check");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		List<Cart> cartList = cartRepository.findByUserId(request.getData().getUserId());

		if(cartList.isEmpty()) {
			int goodsStock = goodsRepository.findById(request.getData().getGoodsIds().get(0)).orElseThrow(NoSuchElementException::new).getGdsStock();
			return goodsStock - request.getData().getCartQuantity()  <= 0 ? Header.ERROR("Out of stock.") : Header.OK();
		} else {
			List<Goods> goodsList= goodsRepository.findAllById(request.getData().getGoodsIds());
			for(Goods goods : goodsList) {
				for(Cart cart : cartList) {
					if(goods.getId().equals(cart.getGoods().getId())) {
						if(goods.getGdsStock() == 0) {
							return Header.ERROR("Out of stock.");
						} else if(goods.getGdsStock() - cart.getCartQuantity() < 0) {
							return Header.ERROR(String.format("The <%s> %s has left.", goods.getGdsName(), goods.getGdsStock()));
						}
					}
				}
			}
		}
		return Header.OK();
	}

	private Optional<Cart> getCart(Long id) {
		return baseRepository.findById(id);
	}
	
	public CartApiResponse response(Cart cart) {

		GoodsApiResponse goodsApiResponse = goodsApiLogicService.response(cart.getGoods());
		List<GoodsImage> goodsImageList = cart.getGoods().getGoodsImageList();
		List<GoodsImageApiResponse> goodsImageApiResponseList = goodsImageList.stream()
				.map(goodsImage -> adminGoodsImageApiLogicService.response(goodsImage))
				.collect(Collectors.toList());
		goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);

		return CartApiResponse.builder()
				.id(cart.getId())
				.cartQuantity(cart.getCartQuantity())
				.cartPrice(cart.getCartPrice())
				.regDate(dateFormat.dateFormat(cart.getRegDate()))
				.upDate(dateFormat.dateFormat(cart.getUpDate()))
				.userApiResponse(userApiLogicService.response(cart.getUser()))
				.goodsApiResponse(goodsApiResponse)
				.build();
	}

}
