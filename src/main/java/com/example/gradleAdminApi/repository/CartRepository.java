package com.example.gradleAdminApi.repository;

import com.example.gradleAdminApi.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findByUserId(Long id);

	Optional<Cart> findByUserIdAndGoodsId(Long userId, Long goodsId);

    void deleteAllByIdIn(List<Long> collect);
}
