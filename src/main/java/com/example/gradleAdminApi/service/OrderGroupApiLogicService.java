package com.example.gradleAdminApi.service;

import com.example.gradleAdminApi.exception.EmailNotExistedException;
import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.Pagination;
import com.example.gradleAdminApi.model.entity.*;
import com.example.gradleAdminApi.model.enumclass.ErrorMessages;
import com.example.gradleAdminApi.model.enumclass.OrderStatus;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.OrderGroupApiRequest;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import com.example.gradleAdminApi.model.network.response.OrderDetailApiResponse;
import com.example.gradleAdminApi.model.network.response.OrderGroupApiResponse;
import com.example.gradleAdminApi.repository.CartRepository;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.repository.OrderDetailRepository;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.service.admin.AdminGoodsImageApiLogicServiceImpl;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderGroupApiLogicService extends AllBaseService<OrderGroupApiRequest, OrderGroupApiResponse, OrderGroup>{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private UserApiLogicServiceImpl userApiLogicService;

	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private OrderDetailApiLogicServiceImpl orderDetailApiLogicService;

	@Autowired
	private GoodsApiLogicServiceImpl goodsApiLogicService;

	@Autowired
	private AdminGoodsImageApiLogicServiceImpl adminGoodsImageApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(readOnly = true)
	public Header<List<OrderGroupApiResponse>> index(Long userId, Pageable pageable, Authentication authentication) throws Exception {
		log.info("get order list");

		jwtUtil.getAuthPermission(userId, authentication);
		Page<OrderGroup> allOrder = allBaseRepository.findAll(pageable);

		List<OrderGroupApiResponse> orderApiResponseList = allOrder.stream()
				.map(this::response)
				.collect(Collectors.toList());

		Pagination pagination = Pagination.builder()
				.totalPages(allOrder.getTotalPages())
				.totalElement(allOrder.getTotalElements())
				.currentPage(allOrder.getNumber())
				.currentElements(allOrder.getNumberOfElements())
				.build();

		return Header.OK(orderApiResponseList, pagination);
	}

	@Override
	@Transactional(rollbackFor = {NoSuchElementException.class, Exception.class})
	public Header<OrderGroupApiResponse> create(Header<OrderGroupApiRequest> request, Authentication authentication) throws Exception {
		log.info("post order register");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		OrderGroupApiRequest orderGroupApiRequest = request.getData();

		String userEmail = jwtUtil.getClaimsData(authentication).get("userEmail").toString();

		Optional<User> user = userRepository.findByUserEmail(userEmail);
		Optional<Goods> reqGoods = goodsRepository.findById(orderGroupApiRequest.getGoodsIds().get(0));

		if(user.isEmpty() || reqGoods.isEmpty()) {
			return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
		}

//		Get Total Quantity, Total Price
//		int totalQuantity = cartGoodsList.stream()
//				.mapToInt(Cart::getCartQuantity).sum();
//
//		BigDecimal totalPrice = cartGoodsList.stream()
//				.map(Cart::getCartPrice)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);

		String orderRcp = orderGroupApiRequest.getUserName() + " " + orderGroupApiRequest.getUserSurname();

		OrderGroup order = OrderGroup.builder()
				.orderNum(generateOrderNumber())
				.orderRcp(orderRcp)
				.postCode(orderGroupApiRequest.getPostCode())
				.userAddr1(orderGroupApiRequest.getUserAddr1())
				.userAddr2(orderGroupApiRequest.getUserAddr2())
				.userAddr3(orderGroupApiRequest.getUserAddr3())
				.phoneNum(orderGroupApiRequest.getPhoneNum())
				.totalPrice(orderGroupApiRequest.getTotalPrice())
				.totalQuantity(orderGroupApiRequest.getTotalQuantity())
				.paymentType(orderGroupApiRequest.getPaymentType())
				.orderStatus(OrderStatus.READY_TO_SHIP)
				.user(user.get())
				.build();

		OrderGroup newOrder = allBaseRepository.save(order);

		if(orderGroupApiRequest.getIsDirectOrder()) {
			OrderDetail orderDetail = OrderDetail.builder()
					.goodsQuantity(orderGroupApiRequest.getTotalQuantity())
					.goodsTotalPrice(orderGroupApiRequest.getTotalPrice())
					.goods(reqGoods.get())
					.orderGroup(newOrder)
					.build();
			orderDetailRepository.save(orderDetail);

			reqGoods.get().setGdsStock(reqGoods.get().getGdsStock() - orderDetail.getGoodsQuantity());
			goodsRepository.save(reqGoods.get());
		} else {
			// Insert Order Detail
			List<Cart> cartGoodsList = cartRepository.findByUserId(user.get().getId());

			cartGoodsList.forEach(cartGoods -> {
				Goods goods = cartGoods.getGoods();

				OrderDetail orderDetail = OrderDetail.builder()
						.goodsQuantity(cartGoods.getCartQuantity())
						.goodsTotalPrice(cartGoods.getGoods().getGdsPrice().multiply(new BigDecimal(cartGoods.getCartQuantity())))
						.goods(goods)
						.orderGroup(newOrder)
						.build();
				orderDetailRepository.save(orderDetail);

				goods.setGdsStock(goods.getGdsStock() - orderDetail.getGoodsQuantity());
				goodsRepository.save(goods);
			});

			cartRepository.deleteAllByIdIn(cartGoodsList.stream().map(Cart::getId).collect(Collectors.toList()));
		}

		return Header.OK(response(newOrder));
	}

	@Override
	@Transactional(readOnly = true)
	public Header<OrderGroupApiResponse> read(Long id, Long userId, Authentication authentication) throws Exception {
		log.info("get order detail");

		jwtUtil.getAuthPermission(userId, authentication);
		// Order
		OrderGroup order = allBaseRepository.findById(id).orElseThrow(NoSuchElementException::new);
		OrderGroupApiResponse orderGroupApiResponse = response(order);

		// OrderDetail
		List<OrderDetail> orderDetailList = order.getOrderDetailList();
		List<OrderDetailApiResponse> orderDetailApiResponseList = orderDetailList.stream()
				.map(orderDetail -> {
					OrderDetailApiResponse orderDetailApiResponse = orderDetailApiLogicService.response(orderDetail);

					// Goods
					GoodsApiResponse goodsApiResponse = goodsApiLogicService.response(orderDetail.getGoods());
					List<GoodsImageApiResponse> goodsImageApiResponseList = orderDetail.getGoods().getGoodsImageList().stream()
							.map(goodsImage -> adminGoodsImageApiLogicService.response(goodsImage)).collect(Collectors.toList());
					goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);
					orderDetailApiResponse.setGoodsApiResponse(goodsApiResponse);

					return orderDetailApiResponse;
				})
				.collect(Collectors.toList());

		orderGroupApiResponse.setOrderDetailApiResponseList(orderDetailApiResponseList);

		return Header.OK(orderGroupApiResponse);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
	public Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> request, Authentication authentication) throws Exception {
		log.info("put order modify");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		OrderGroupApiRequest orderGroupApiRequest = request.getData();

		Optional<OrderGroup> order = getOrder(orderGroupApiRequest.getId());

		return order.isEmpty() ? Header.ERROR(ErrorMessages.NOT_FOUND.getTitle()) :
				Header.OK(response(allBaseRepository.save(order.get().setOrderStatus(OrderStatus.CANCELLED))));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {NoSuchElementException.class, Exception.class}, isolation = Isolation.READ_COMMITTED)
	public Header delete(Long id, Authentication authentication) throws Exception {
		log.info("delete order");

		jwtUtil.getAuthPermission(id, authentication);
		Optional<OrderGroup> Order = getOrder(id);

		if(Order.isPresent()) {
			allBaseRepository.save(Order.get().setOrderStatus(OrderStatus.CANCELLED));
			return Header.OK();
		}
		return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
	}

	private String generateOrderNumber() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String ymd = year + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1) +
				new DecimalFormat("00").format(cal.get(Calendar.DATE));
		StringBuilder subNum = new StringBuilder();

		for(int i = 1; i <= 6; i++) {
			subNum.append((int) (Math.random() * 10));
		}

		return ymd + "_" + subNum;
	}

	private Optional<OrderGroup> getOrder(Long id) {
		return allBaseRepository.findById(id);
	}

	public OrderGroupApiResponse response(OrderGroup orderGroup) {

		return OrderGroupApiResponse.builder()
				.id(orderGroup.getId())
				.orderNum(orderGroup.getOrderNum())
				.orderRcp(orderGroup.getOrderRcp())
				.postCode(orderGroup.getPostCode())
				.userAddr1(orderGroup.getUserAddr1())
				.userAddr2(orderGroup.getUserAddr2())
				.userAddr3(orderGroup.getUserAddr3())
				.phoneNum(orderGroup.getPhoneNum())
				.totalPrice(orderGroup.getTotalPrice())
				.totalQuantity(orderGroup.getTotalQuantity())
				.paymentType(orderGroup.getPaymentType())
				.orderStatus(orderGroup.getOrderStatus().getTitle())
				.orderDate(dateFormat.dateFormat(orderGroup.getOrderDate()))
				.upDate(dateFormat.dateFormat(orderGroup.getUpDate()))
				.userApiResponse(userApiLogicService.response(orderGroup.getUser()))
				.build();
	}
}
