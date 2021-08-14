package com.example.gradleAdminApi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gradleAdminApi.model.entity.Goods;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long>{
    Page<Goods> findByGdsNameContaining(String keyword, Pageable pageable);

    List<Goods> findByCategoryId(Long id);

    Page<Goods> findAllByCateCodeIn(List<String> targetCateCodeList, Pageable pageable);
}
