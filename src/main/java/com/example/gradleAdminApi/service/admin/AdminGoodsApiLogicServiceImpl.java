package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.exception.GoogleStorageException;
import com.example.gradleAdminApi.exception.NoSuchElementException;
import com.example.gradleAdminApi.exception.UnauthenticatedException;
import com.example.gradleAdminApi.model.Pagination;
import com.example.gradleAdminApi.model.entity.Category;
import com.example.gradleAdminApi.model.entity.Goods;
import com.example.gradleAdminApi.model.entity.GoodsImage;
import com.example.gradleAdminApi.model.entity.OrderDetail;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.model.network.request.GoodsApiRequest;
import com.example.gradleAdminApi.model.network.response.GoodsApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsDetailApiResponse;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import com.example.gradleAdminApi.repository.*;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.HandlingGCPSImages;
import com.example.gradleAdminApi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminGoodsApiLogicServiceImpl implements AdminGoodsApiLogicService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private GoodsRepository goodsRepository;
	
	@Autowired
	private GoodsImageRepository goodsImageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private AdminCategoryApiLogicService adminCategoryApiLogicService;
	
	@Autowired
	private AdminGoodsImageApiLogicServiceImpl adminGoodsImageApiLogicServiceImpl;
	
//	@Autowired
//	private ImagesUpload imagesUpload;

	@Autowired
	private HandlingGCPSImages handlingGCPSImages;

	@Autowired
	private DateFormat dateFormat;

	@Override
	@Transactional(readOnly = true)
	public Header<List<GoodsApiResponse>> index(Pageable pageable, Authentication authentication, String keyword) throws Exception, IOException {
		log.info("get goods list");

		jwtUtil.getAccessAllPermission(authentication);
		Page<Goods> allGoods = goodsRepository.findByGdsNameContaining(keyword, pageable);

		List<GoodsApiResponse> goodsApiResponsesList = allGoods.stream()
				.map(goods -> {
					GoodsApiResponse goodsApiResponse = response(goods);

					// Category
					goodsApiResponse.setCategoryApiResponse(adminCategoryApiLogicService.response(goods.getCategory()));


					// Goods Images
					List<GoodsImage> goodsImageList = goods.getGoodsImageList();
					List<GoodsImageApiResponse> GoodsImageApiResponseList = goodsImageList.stream()
							.map(goodsImage -> adminGoodsImageApiLogicServiceImpl.response(goodsImage))
							.collect(Collectors.toList());
					goodsApiResponse.setGoodsImageApiResponseList(GoodsImageApiResponseList);

					return goodsApiResponse;
				})
				.collect(Collectors.toList());

		Pagination pagination = Pagination.builder()
				.totalPages(allGoods.getTotalPages())
				.totalElement(allGoods.getTotalElements())
				.currentPage(allGoods.getNumber())
				.currentElements(allGoods.getNumberOfElements())
				.build();

		return Header.OK(goodsApiResponsesList, pagination);
	}

	@Override
	@Transactional(rollbackFor = {IOException.class, Exception.class})
	public Header<GoodsApiResponse> create(Header<GoodsApiRequest> request, Authentication authentication, List<MultipartFile> mpRequest) throws IOException {
		log.info("post goods register");

		jwtUtil.getAccessAllPermission(authentication);
		GoodsApiRequest goodsApiRequest = request.getData();

		// Insert to Goods Table
		Goods goods = Goods.builder()
				.cateCode(goodsApiRequest.getCateCode())
				.gdsName(goodsApiRequest.getGdsName())
				.gdsPrice(goodsApiRequest.getGdsPrice())
				.gdsStock(goodsApiRequest.getGdsStock())
				.gdsDesc(goodsApiRequest.getGdsDesc())
				.build();

		goods.setCategory(categoryRepository.findByCateCode(goodsApiRequest.getCateCode()).orElseThrow(NoSuchElementException::new));

		Goods newGoods = goodsRepository.save(goods);

		if(mpRequest != null) {
			mpRequest.forEach(image -> {
				String originalFilename = image.getOriginalFilename();
				String uuidFileName = handlingGCPSImages.makeFilenameWithUUID(originalFilename);

				GoodsImage goodsImage = new GoodsImage();
				goodsImage.setGoods(newGoods);
				goodsImage.setOriName(originalFilename);
				goodsImage.setImgSize(image.getSize());
				String imageName = handlingGCPSImages.executeImageUpload(image, uuidFileName);
				goodsImage.setImgName(imageName);
				goodsImageRepository.save(goodsImage);
			});
		} else {
			GoodsImage goodsImage = new GoodsImage();

//			goodsImage.setGdsImg("none.png");
//			goodsImage.setGdsThumbImg("none.png");
			goodsImage.setGoods(newGoods);
			goodsImage.setOriName("none.jpg");
			goodsImage.setImgName("none.jpg");
			goodsImageRepository.save(goodsImage);
		}

		// Insert to Goods Image Table
//		Map<String, String> filePath;
//
//		if(!mpRequest.isEmpty()) {
//			for (MultipartFile image : mpRequest) {
//				GoodsImage goodsImage = new GoodsImage();
//				goodsImage.setGoods(newGoods);
//
//				filePath = imagesUpload.imgUpload(image.getOriginalFilename(), image.getBytes());
//
//				String ymdPath = filePath.get("ymdPath").replaceAll("\\\\", "/");
//				String newFileName = filePath.get("newFileName").replaceAll("\\\\", "/");
//
//				goodsImage.setGdsImg("imgUpload" + ymdPath + "/" + newFileName);
//				goodsImage.setGdsThumbImg("imgUpload" + ymdPath + "/s/s_" + newFileName);
//				goodsImage.setOriName(image.getOriginalFilename());
//				goodsImage.setImgSize(image.getSize());
//				goodsImageRepository.save(goodsImage);
//			}
//		} else {
//			GoodsImage goodsImage = new GoodsImage();
//
//			goodsImage.setGdsImg("imgUpload/none.png");
//			goodsImage.setGdsThumbImg("imgUpload/none.png");
//			goodsImage.setOriName("none");
//			goodsImageRepository.save(goodsImage);
//		}
		return Header.OK();
	}

	@Override
	@Transactional(readOnly = true)
	public Header<GoodsDetailApiResponse> read(Long id, Authentication authentication) throws Exception {
		log.info("get goods detail");

		jwtUtil.getAccessAllPermission(authentication);
		// Goods
		Goods goods = goodsRepository.findById(id).orElseThrow(NoSuchElementException::new);
		GoodsApiResponse goodsApiResponse = response(goods);

		// Category
		goodsApiResponse.setCategoryApiResponse(adminCategoryApiLogicService.response(goods.getCategory()));

		// Goods Images
		List<GoodsImageApiResponse> goodsImageApiResponseList = goodsImageRepository.findByGoodsIdAndImgFlgIs(id,0).stream()
				.map(goodsImage -> adminGoodsImageApiLogicServiceImpl.response(goodsImage))
				.collect(Collectors.toList());
		goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);

		GoodsDetailApiResponse goodsDetailApiResponse = GoodsDetailApiResponse.builder()
				.goodsApiResponse(goodsApiResponse)
				.build();

		return Header.OK(goodsDetailApiResponse);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Header<GoodsDetailApiResponse> update(Header<GoodsApiRequest> request, Authentication authentication, List<MultipartFile> mpRequest) throws Exception {
		log.info("put goods modify");

		jwtUtil.getAccessAllPermission(authentication);
		GoodsApiRequest goodsApiRequest = request.getData();

		// For Response Object
		GoodsApiResponse goodsApiResponse = new GoodsApiResponse();

		// Category
		Category reCategory = categoryRepository.findByCateCode(goodsApiRequest.getCateCode()).orElseThrow(NoSuchElementException::new);

		// For Response Category
		goodsApiResponse.setCategoryApiResponse(adminCategoryApiLogicService.response(reCategory));

		// Goods
		Goods reGoods = goodsRepository.findById(goodsApiRequest.getId()).orElseThrow(NoSuchElementException::new)
				.setGdsName(goodsApiRequest.getGdsName())
				.setCateCode(goodsApiRequest.getCateCode())
				.setGdsPrice(goodsApiRequest.getGdsPrice())
				.setGdsStock(goodsApiRequest.getGdsStock())
				.setGdsDesc(goodsApiRequest.getGdsDesc())
				.setCategory(reCategory);

		// For Response Goods
		goodsApiResponse = response(goodsRepository.save(reGoods));

		// Update Image Delete Flag
		// Delete Google Storage Image
		goodsImageRepository.findAllById(request.getData().getDelImageIds()).forEach(
			image -> {
				image.setImgFlg(1);
				goodsImageRepository.save(image);
				Boolean result = handlingGCPSImages.executeImageDelete(image.getImgName());
				if(!result) {
					throw new GoogleStorageException();
				}
			}
		);
//			request.getData().getDelImageIds().forEach(imgId -> goodsImageRepository.deleteById(imgId));

		// Update Google Storage Image
		// Update Database Image
		if(mpRequest != null) {
			mpRequest.forEach(updateImage -> {
				String originalFilename = updateImage.getOriginalFilename();
				String updateImageName = handlingGCPSImages.makeFilenameWithUUID(originalFilename);

				GoodsImage upGoodsImage = new GoodsImage();
				upGoodsImage.setGoods(reGoods);
				upGoodsImage.setOriName(originalFilename);
				upGoodsImage.setImgName(updateImageName);
				upGoodsImage.setImgSize(updateImage.getSize());
				goodsImageRepository.save(upGoodsImage);
				handlingGCPSImages.executeImageUpload(updateImage, updateImageName);
			});
		}

		// Not exist Images in database
		List<GoodsImage> goodsImageList = goodsImageRepository.findByGoodsIdAndImgFlgIs(request.getData().getId(),0);
		if(goodsImageList.isEmpty()) {
			GoodsImage noneGoodsImage = new GoodsImage();
			noneGoodsImage.setGdsImg("imgUpload/none.png");
			noneGoodsImage.setGdsThumbImg("imgUpload/none.png");
			noneGoodsImage.setImgName("none");
			noneGoodsImage.setOriName("none");
			noneGoodsImage.setGoods(reGoods);
			goodsImageRepository.save(noneGoodsImage);
		}

		// Update Images
//		GoodsImage reGoodsImage = new GoodsImage();
//
//		if(mpRequest != null) {
//			Map<String, String> filePath;
//
//			for(MultipartFile image : mpRequest) {
//				if(image.getOriginalFilename() != null) {
//					try {
//						filePath = imagesUpload.imgUpload(image.getOriginalFilename(), image.getBytes());
//
//						String ymdPath = filePath.get("ymdPath").replaceAll("\\\\", "/");
//						String newFileName = filePath.get("newFileName").replaceAll("\\\\", "/");
//
//						reGoodsImage.setGdsImg("imgUpload" + ymdPath + "/" + newFileName);
//						reGoodsImage.setGdsThumbImg("imgUpload" + ymdPath + "/s/s_" + newFileName);
//						reGoodsImage.setOriName(image.getOriginalFilename());
//						reGoodsImage.setImgSize(image.getSize());
//						reGoodsImage.setGoods(reGoods);
//						goodsImageRepository.save(reGoodsImage);
//					} catch (Exception e) {
//						throw new IOException();
//					}
//				}
//			}
//		}
//

		// For Response Goods Images
		List<GoodsImageApiResponse> goodsImageApiResponseList = goodsImageList.stream()
				.map(goodsImage -> adminGoodsImageApiLogicServiceImpl.response(goodsImage))
				.collect(Collectors.toList());
		goodsApiResponse.setGoodsImageApiResponseList(goodsImageApiResponseList);

		// For Response Goods Category
		Category category = categoryRepository.findByCateCode(goodsApiRequest.getCateCode()).orElseThrow(NoSuchElementException::new);
		goodsApiResponse.setCategoryApiResponse(adminCategoryApiLogicService.response(category));

		// For Response Detailed Goods
		GoodsDetailApiResponse goodsDetailApiResponse = GoodsDetailApiResponse.builder()
				.goodsApiResponse(goodsApiResponse)
				.build();

		return Header.OK(goodsDetailApiResponse);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor = {NoSuchElementException.class, Exception.class})
	public Header delete(Long id, Authentication authentication) throws Exception {
		log.info("delete goods");

		jwtUtil.getAccessAllPermission(authentication);

		// Delete Image files
		List<GoodsImage> goodsImageList = goodsRepository.findById(id).orElseThrow(NoSuchElementException::new).getGoodsImageList();
		goodsImageList.forEach(image -> {
			Boolean result = handlingGCPSImages.executeImageDelete(image.getImgName());
			if (!result) {
				throw new GoogleStorageException();
			}
		});
//		imagesUpload.deleteImages(goodsImageList);

		goodsImageList.forEach(image -> {
			List<OrderDetail> orderDetailList = image.getGoods().getOrderDetailList();
			orderDetailList.forEach(orderDetail -> {
				orderDetail.setGoods(null);
				orderDetailRepository.save(orderDetail);
			});
		});

		// Delete Image data in database
		goodsRepository.delete(goodsRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return Header.OK();
	}
	
	public GoodsApiResponse response(Goods goods) {
		
		return GoodsApiResponse.builder()
				.id(goods.getId())
				.gdsName(goods.getGdsName())
				.cateCode(goods.getCateCode())
				.gdsPrice(String.valueOf(goods.getGdsPrice()))
				.gdsStock(String.valueOf(goods.getGdsStock()))
				.gdsDesc(goods.getGdsDesc())
				.regDate(dateFormat.dateFormat(goods.getRegDate()))
				.upDate(dateFormat.dateFormat(goods.getUpDate()))
				.build();
	}
}
