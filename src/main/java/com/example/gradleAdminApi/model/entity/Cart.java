package com.example.gradleAdminApi.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "goods"})
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Cart {
//	  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	  `cart_quantity` INT NOT NULL,
//	  `cart_price` DECIMAL(20,4) NOT NULL,
//	  `reg_date` DATETIME NOT NULL,
//	  `user_id` BIGINT(20) NOT NULL,
//	  `goods_id` BIGINT(20) NOT NULL,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int cartQuantity;

	private BigDecimal cartPrice;

	@CreatedDate
	private LocalDateTime regDate;

	@LastModifiedDate
	private LocalDateTime upDate;

	@ManyToOne
	private User user;

	@ManyToOne
	private Goods goods;
}
