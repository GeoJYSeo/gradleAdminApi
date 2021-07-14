package com.example.gradleAdminApi.service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.enumclass.ErrorMessages;
import com.example.gradleAdminApi.repository.CommentRepository;
import com.example.gradleAdminApi.service.admin.AdminGoodsImageApiLogicServiceImpl;
import com.example.gradleAdminApi.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.entity.Goods;
import com.example.gradleAdminApi.model.entity.Comment;
import com.example.gradleAdminApi.model.entity.User;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.CommentApiRequest;
import com.example.gradleAdminApi.model.network.response.CommentApiResponse;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.repository.UserRepository;
import com.example.gradleAdminApi.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CommentApiLogicServiceImpl extends BaseService<CommentApiRequest, CommentApiResponse, Comment> implements CommentApiLogicService {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserApiLogicServiceImpl userApiLogicService;
	
	@Autowired
	private GoodsApiLogicServiceImpl goodsApiLogicService;

	@Autowired
	private AdminGoodsImageApiLogicServiceImpl adminGoodsImageApiLogicService;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CommentApiResponse> create(Header<CommentApiRequest> request, Authentication authentication) throws Exception {
		log.info("post comment register");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		CommentApiRequest commentApiRequest = request.getData();

		Optional<User> user = userRepository.findById(commentApiRequest.getUserId());
		Optional<Goods> goods = goodsRepository.findById(commentApiRequest.getGoodsId());

		if(user.isEmpty() || goods.isEmpty()) {
			return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
		}

		Comment comment = Comment.builder()
				.comment(commentApiRequest.getComment())
				.user(user.get())
				.goods(goods.get())
				.build();

		Comment newComment = baseRepository.save(comment);

		return Header.OK(response(newComment));
	}

	@Override
	@Transactional(readOnly = true)
	public Header<List<CommentApiResponse>> readWithGoodsId(Long id) throws Exception {
		log.info("get comment detail with goods id");

		List<CommentApiResponse> commentApiResponseList = commentRepository.findByGoodsIdOrderByRegDateDesc(id).orElseGet(Collections::emptyList)
				.stream().map(this::response)
				.collect(Collectors.toList());

		return Header.OK(commentApiResponseList);
	}

	@Override
	@Transactional(readOnly = true)
	public Header<List<CommentApiResponse>> readWithUserId(Long id, Authentication authentication) throws Exception {
		log.info("get comment detail with user id");

		jwtUtil.getAuthPermission(id, authentication);
		List<CommentApiResponse> commentApiResponseList = commentRepository.findByUserIdOrderByRegDateDesc(id).orElseGet(Collections::emptyList)
				.stream().map(comment -> {
					CommentApiResponse commentApiResponse = response(comment);
					commentApiResponse.getGoodsApiResponse()
							.setGoodsImageApiResponseList(List.of(adminGoodsImageApiLogicService.response(comment.getGoods().getGoodsImageList().get(0))));
					return commentApiResponse;
				})
				.collect(Collectors.toList());

		return Header.OK(commentApiResponseList);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CommentApiResponse> update(Header<CommentApiRequest> request, Authentication authentication) throws Exception {
		log.info("put comment modify");

		jwtUtil.getAuthPermission(request.getData().getUserId(), authentication);
		CommentApiRequest commentApiRequest = request.getData();

		Optional<Comment> comment = getComment(commentApiRequest.getId());

		return comment.isEmpty() ? Header.ERROR(ErrorMessages.NOT_FOUND.getTitle()) :
				Header.OK(response(baseRepository.save(comment.get().setComment(commentApiRequest.getComment()))));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header delete(Long id, Long userId, Authentication authentication) throws Exception {
		log.info("delete comment");

		jwtUtil.getAuthPermission(userId, authentication);
		Optional<Comment> comment = getComment(id);

		if(comment.isPresent()) {
			baseRepository.delete(comment.get());
			return Header.OK();
		}
		return Header.ERROR(ErrorMessages.NOT_FOUND.getTitle());
	}

	private Optional<Comment> getComment(Long id) {
		return baseRepository.findById(id);
	}

	public CommentApiResponse response(Comment comment) {

		String email = comment.getUser().getUserEmail();

		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String formattedDate = comment.getRegDate().format(myFormatObj);

		String commentLabel = "<" + email.substring(0, email.indexOf("@")) + "> " + formattedDate;

		return CommentApiResponse.builder()
				.id(comment.getId())
				.comment(comment.getComment())
				.commentLabel(commentLabel)
				.regDate(dateFormat.dateFormat(comment.getRegDate()))
				.upDate(dateFormat.dateFormat(comment.getUpDate()))
				.userApiResponse(userApiLogicService.response(comment.getUser()))
				.goodsApiResponse(goodsApiLogicService.response(comment.getGoods()))
				.build();
	}
}
