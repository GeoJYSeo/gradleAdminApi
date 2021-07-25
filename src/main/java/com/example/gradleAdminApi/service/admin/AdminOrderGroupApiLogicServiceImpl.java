package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.Pagination;
import com.example.gradleAdminApi.model.entity.GoodsKey;
import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.entity.OrderGroup;
import com.example.gradleAdminApi.model.enumclass.OrderStatus;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.OrderGroupApiRequest;
import com.example.gradleAdminApi.model.network.response.*;
import com.example.gradleAdminApi.repository.GoodsKeyRepository;
import com.example.gradleAdminApi.repository.OrderDetailRepository;
import com.example.gradleAdminApi.repository.OrderGroupRepository;
import com.example.gradleAdminApi.service.GoodsApiLogicServiceImpl;
import com.example.gradleAdminApi.service.GoodsKeyApiLogicServiceImpl;
import com.example.gradleAdminApi.service.UserApiLogicServiceImpl;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AdminOrderGroupApiLogicServiceImpl implements AdminOrderGroupApiLogicService {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private OrderGroupRepository orderGroupRepository;

	@Autowired
	private GoodsKeyRepository goodsKeyRepository;
	
	@Autowired
	private AdminOrderDetailApiLogicServiceImpl adminOrderDetailApiLogicService;
	
	@Autowired
	private AdminGoodsApiLogicServiceImpl adminGoodsApiLogicService;
	
	@Autowired
	private UserApiLogicServiceImpl userApiLogicService;

	@Autowired
	private AdminGoodsImageApiLogicServiceImpl adminGoodsImageApiLogicService;

	@Autowired
	private GoodsKeyApiLogicServiceImpl goodsKeyApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(readOnly = true)
	public Header<List<OrderGroupApiResponse>> index(Pageable pageable, Authentication authentication) throws Exception {
		log.info("get admin order list");

		jwtUtil.getAccessAllPermission(authentication);
		Page<OrderGroup> allOrder = orderGroupRepository.findAll(pageable);

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
	@Transactional(readOnly = true)
	public Header<OrderGroupApiResponse>read(Long id, Authentication authentication) throws Exception {
		log.info("get admin order detail");

		jwtUtil.getAccessAllPermission(authentication);
		// Order
		OrderGroup order = orderGroupRepository.findById(id).orElseThrow(NoSuchElementException::new);
		OrderGroupApiResponse orderGroupApiResponse = response(order);

		// OrderDetail
		List<OrderDetail> orderDetailList = order.getOrderDetailList();
		List<OrderDetailApiResponse> orderDetailResponseList = orderDetailList.stream()
				.map(orderDetail -> {
					List<GoodsKeyApiResponse> goodsKeyApiResponseList = orderDetail.getGoodsKeyList().stream()
							.map(goodsKey -> goodsKeyApiLogicService.response(goodsKey, true)).collect(Collectors.toList());
					OrderDetailApiResponse orderDetailApiResponse = adminOrderDetailApiLogicService.response(orderDetail, goodsKeyApiResponseList);

					// Goods
					GoodsApiResponse goodsApiResponse = adminGoodsApiLogicService.response(orderDetail.getGoods());
					List<GoodsImageApiResponse> goodsImageApiResponseList = orderDetail.getGoods().getGoodsImageList().stream()
							.map(goodsImage -> adminGoodsImageApiLogicService.response(goodsImage)).collect(Collectors.toList());
					goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);
					orderDetailApiResponse.setGoodsApiResponse(goodsApiResponse);

					return orderDetailApiResponse;
				})
				.collect(Collectors.toList());

		orderGroupApiResponse.setOrderDetailApiResponseList(orderDetailResponseList);

		return Header.OK(orderGroupApiResponse);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
	public Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> request, Authentication authentication) throws Exception {
		log.info("put admin order modify");

		jwtUtil.getAccessAllPermission(authentication);
		OrderGroupApiRequest orderGroupApiRequest = request.getData();

		OrderGroup reOrderGroup = orderGroupRepository.findById(orderGroupApiRequest.getId()).orElseThrow(NoSuchElementException::new)
				.setOrderStatus(OrderStatus.CANCELLED);

		reOrderGroup.getOrderDetailList().forEach(orderDetail -> {
			orderDetail.getGoodsKeyList().forEach(goodsKey -> goodsKeyRepository.save(goodsKey.setStatus(OrderStatus.CANCELLED)));
		});

		return Header.OK(response(orderGroupRepository.save(reOrderGroup)));
	}

	@Override
	@Transactional(rollbackFor = {NoSuchElementException.class, Exception.class}, isolation = Isolation.READ_COMMITTED)
	@SuppressWarnings("rawtypes")
	public Header delete(Long id, Authentication authentication) throws Exception {
		log.info("delete admin order");

		jwtUtil.getAccessAllPermission(authentication);
//		orderGroupRepository.delete(orderGroupRepository.findById(id).orElseThrow(NoSuchElementException::new));
		orderGroupRepository.save(orderGroupRepository.findById(id).orElseThrow(NoSuchElementException::new)
				.setOrderStatus(OrderStatus.CANCELLED));

		return Header.OK();
	}

	public OrderGroupApiResponse response(OrderGroup order) {

		return OrderGroupApiResponse.builder()
				.id(order.getId())
				.orderNum(order.getOrderNum())
				.orderRcp(order.getOrderRcp())
				.postCode(order.getPostCode())
				.userAddr1(order.getUserAddr1())
				.userAddr2(order.getUserAddr2())
				.userAddr3(order.getUserAddr3())
				.phoneNum(order.getPhoneNum())
				.totalPrice(order.getTotalPrice())
				.totalQuantity(order.getTotalQuantity())
				.paymentType(order.getPaymentType())
				.orderStatus(order.getOrderStatus().getTitle())
				.orderDate(dateFormat.dateFormat(order.getOrderDate()))
				.upDate(dateFormat.dateFormat(order.getUpDate()))
				.userApiResponse(userApiLogicService.response(order.getUser()))
				.build();
	}
}
