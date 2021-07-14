package com.example.gradleAdminApi.service.admin;

import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.utils.DateFormat;
import com.example.gradleAdminApi.utils.HandlingGCPSImages;
import com.example.gradleAdminApi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.gradleAdminApi.model.entity.GoodsImage;
import com.example.gradleAdminApi.model.network.response.GoodsImageApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class AdminGoodsImageApiLogicServiceImpl implements AdminGoodsImageApiLogicService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private DateFormat dateFormat;

	@Autowired
	private HandlingGCPSImages handlingGCPSImages;

	@Override
	public Header create(Authentication authentication, MultipartFile mpRequest) throws IOException {
		jwtUtil.getAccessAllPermission(authentication);
//		uploadImageToGCPS.executeImageUpload(mpRequest);
		return Header.OK();
	}

	@Override
	public Header update(Authentication authentication, MultipartFile mpRequest) throws IOException {
		jwtUtil.getAccessAllPermission(authentication);
		return Header.OK();
	}

	@Override
	public Header delete(Authentication authentication, Long id) throws IOException {
		jwtUtil.getAccessAllPermission(authentication);
//		return uploadImageToGCPS.executeImageDelete(id) ? Header.OK() : Header.ERROR("Failed Delete image");
		return Header.OK();
	}

	@Override
	public GoodsImageApiResponse response(GoodsImage goodsImage) {
		try {
			return GoodsImageApiResponse.builder()
					.id(goodsImage.getId())
					.imgName(handlingGCPSImages.generateV4GetObjectSignedUrl(goodsImage.getImgName()))
					.oriName(goodsImage.getOriName())
					.imgSize(goodsImage.getImgSize())
					.imgFlg(goodsImage.getImgFlg())
					.goodsId(goodsImage.getGoods().getId())
					.regDate(dateFormat.dateFormat(goodsImage.getRegDate()))
					.upDate(dateFormat.dateFormat(goodsImage.getUpDate()))
					.build();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
