package com.example.gradleAdminApi.service.admin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.enumclass.ErrorMessages;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.exception.EmailNotExistedException;
import com.example.gradleAdminApi.model.Pagination;
import com.example.gradleAdminApi.model.entity.Goods;
import com.example.gradleAdminApi.model.entity.Comment;
import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.CommentApiRequest;
import com.example.gradleAdminApi.model.network.response.CommentApiResponse;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.service.AdminBaseService;
import com.example.gradleAdminApi.service.GoodsApiLogicServiceImpl;
import com.example.gradleAdminApi.service.UserApiLogicServiceImpl;
import com.example.gradleAdminApi.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AdminCommentApiLogicService extends AdminBaseService<CommentApiRequest, CommentApiResponse, Comment>{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GoodsRepository goodsRepository;
	
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
	public Header<List<CommentApiResponse>> index(
			@PageableDefault(sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) throws Exception {
		log.info("get admin comment list");

		jwtUtil.getAccessAllPermission(authentication);
		Page<Comment> allComment = adminBaseRepository.findAll(pageable);

		List<CommentApiResponse> commentApiResponses = allComment.stream()
				.map(this::response)
				.collect(Collectors.toList());

		Pagination pagination = Pagination.builder()
				.totalPages(allComment.getTotalPages())
				.totalElement(allComment.getTotalElements())
				.currentPage(allComment.getNumber())
				.currentElements(allComment.getNumberOfElements())
				.build();

		return Header.OK(commentApiResponses, pagination);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CommentApiResponse> create(Header<CommentApiRequest> request, Authentication authentication) throws Exception {
		log.info("post admin comment register");

		jwtUtil.getAccessAllPermission(authentication);
		CommentApiRequest commentApiResponses = request.getData();

		String userEmail = jwtUtil.getClaimsData(authentication).get("userEmail").toString();

		Optional<User> user = userRepository.findByUserEmail(userEmail);
		Optional<Goods> goods = goodsRepository.findById(commentApiResponses.getGoodsId());

		if(user.isEmpty() || goods.isEmpty()) {
			return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
		}

		Comment comment = Comment.builder()
				.comment(commentApiResponses.getComment())
				.user(user.get())
				.goods(goods.get())
				.build();

		return Header.OK(response(adminBaseRepository.save(comment)));
	}

	@Override
	@Transactional(readOnly = true)
	public Header<CommentApiResponse> read(Long id, Authentication authentication) throws Exception {
		log.info("get admin comment detail");

		jwtUtil.getAccessAllPermission(authentication);
		return Header.OK(response(adminBaseRepository.findById(id).orElseThrow(NoSuchElementException::new)));
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CommentApiResponse> update(Header<CommentApiRequest> request, Authentication authentication) {
		log.info("put admin comment modify");

		jwtUtil.getAccessAllPermission(authentication);
		CommentApiRequest commentApiResponses = request.getData();

		Optional<Comment> selectedComment = getComment(commentApiResponses.getId());


		return selectedComment.isEmpty() ? Header.ERROR(ErrorMessages.NOT_FOUND.getTitle()) :
				Header.OK(response(adminBaseRepository.save(selectedComment.get().setComment(commentApiResponses.getComment()))));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header delete(Long id, Authentication authentication) throws Exception {
		log.info("delete admin comment");

		jwtUtil.getAccessAllPermission(authentication);
		Optional<Comment> comment = getComment(id);

		if(comment.isPresent()) {
			adminBaseRepository.delete(comment.get());
			return Header.OK();
		}
		return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
	}

	private Optional<Comment> getComment(Long id) {
		return adminBaseRepository.findById(id);
	}

	public CommentApiResponse response(Comment comment) {

		List<GoodsImageApiResponse> goodsImageApiResponseList = comment.getGoods().getGoodsImageList().stream()
				.map(goodsImage -> adminGoodsImageApiLogicService.response(goodsImage))
				.collect(Collectors.toList());

		GoodsApiResponse goodsApiResponse = goodsApiLogicService.response(comment.getGoods());
		goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);

		return CommentApiResponse.builder()
				.id(comment.getId())
				.comment(comment.getComment())
				.regDate(dateFormat.dateFormat(comment.getRegDate()))
				.upDate(dateFormat.dateFormat(comment.getUpDate()))
				.userApiResponse(userApiLogicService.response(comment.getUser()))
				.goodsApiResponse(goodsApiResponse)
				.build();
	}
}
