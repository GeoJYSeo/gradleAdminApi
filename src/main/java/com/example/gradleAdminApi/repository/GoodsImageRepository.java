package com.example.gradleAdminApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gradleAdminApi.model.entity.GoodsImage;

@Repository
public interface GoodsImageRepository extends JpaRepository<GoodsImage, Long> {

	List<GoodsImage> findByGoodsId(Long id);

    List<GoodsImage> findByGoodsIdAndImgFlgIs(Long id, int i);
}
