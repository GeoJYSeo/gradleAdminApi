package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.entity.Category;
import com.example.gradleAdminApi.model.entity.Goods;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.CategoryApiRequest;
import com.example.gradleAdminApi.model.network.response.CategoryApiResponse;
import com.example.gradleAdminApi.repository.CategoryRepository;
import com.example.gradleAdminApi.repository.GoodsRepository;
import com.example.gradleAdminApi.service.AdminBaseService;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminCategoryApiLogicService extends AdminBaseService<CategoryApiRequest, CategoryApiResponse, Category> {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private DateFormat dateFormat;

	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	@Transactional(readOnly = true)
	public Header<List<CategoryApiResponse>> index(
			@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable, Authentication authentication) throws Exception {
		log.info("get category list");
		
		jwtUtil.getAccessAllPermission(authentication);

		List<Category> categoryList = adminBaseRepository.findAll();

		List<CategoryApiResponse> categoryApiResponses = categoryList.stream()
				.map(this::response)
				.collect(Collectors.toList());

		return Header.OK(categoryApiResponses);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CategoryApiResponse> create(Header<CategoryApiRequest> request, Authentication authentication) throws Exception {
		log.info("post category register");

		jwtUtil.getAccessAllPermission(authentication);
		CategoryApiRequest categoryApiRequest = request.getData();

		Category category = Category.builder()
				.cateName(categoryApiRequest.getCateName())
				.cateCode(categoryApiRequest.getCateCode())
				.cateCodeRef(categoryApiRequest.getCateCodeRef())
				.build();

		Category newCategory = adminBaseRepository.save(category);

		return Header.OK(response(newCategory));
	}

	@Override
	@Transactional(readOnly = true)
	public Header<CategoryApiResponse> read(Long id, Authentication authentication) throws Exception {
		log.info("get category detail");

		jwtUtil.getAccessAllPermission(authentication);
		return  Header.OK(response(adminBaseRepository.findById(id).orElseThrow(NoSuchElementException::new)));
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<CategoryApiResponse> update(Header<CategoryApiRequest> request, Authentication authentication) throws Exception {
		log.info("put category modify");

		jwtUtil.getAccessAllPermission(authentication);
		CategoryApiRequest categoryApiRequest = request.getData();

		Category selectedCategory = adminBaseRepository.findById(categoryApiRequest.getId()).orElseThrow(NoSuchElementException::new)
				.setCateName(categoryApiRequest.getCateName())
				.setCateCode(categoryApiRequest.getCateCode())
				.setCateCodeRef(categoryApiRequest.getCateCodeRef());

		return Header.OK(response(adminBaseRepository.save(selectedCategory)));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {NoSuchElementException.class, Exception.class})
	public Header delete(Long id, Authentication authentication) throws Exception {
		log.info("delete category");

		jwtUtil.getAccessAllPermission(authentication);
		goodsRepository.findByCategoryId(id).forEach(goods -> {
			Category targetCate = adminBaseRepository.findById(1L).orElseThrow(NoSuchElementException::new);
			goods.setCategory(targetCate);
			goods.setCateCode(targetCate.getCateCode());
			goodsRepository.save(goods);
		});
		adminBaseRepository.delete(adminBaseRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return Header.OK();
	}

	public CategoryApiResponse response(Category category) {
		
		return CategoryApiResponse.builder()
				.id(category.getId())
				.cateName(category.getCateName())
				.cateCode(category.getCateCode())
				.cateCodeRef(category.getCateCodeRef())
				.regDate(dateFormat.dateFormat(category.getRegDate()))
				.build();
	}

	private void accept(Goods goods) {
		goodsRepository.save(goods.setCategory(categoryRepository.findByCateCode("0").orElseThrow(NoSuchElementException::new)));
	}
}
