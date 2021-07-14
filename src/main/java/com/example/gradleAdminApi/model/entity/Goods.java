package com.example.gradleAdminApi.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
@ToString(exclude = {"orderDetailList", "goodsImageList", "commentList", "category", "cartList"})
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Goods {
//	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	`gds_name` VARCHAR(50) NOT NULL,
//	`cate_code` VARCHAR(30) NOT NULL,
//	`gds_price` DECIMAL(12,4) NOT NULL,
//	`gds_stock` INT NOT NULL,
//	`gds_desc` TEXT NOT NULL,
//	`reg_date` DATETIME NOT NULL,
//	`up_date` DATETIME NULL,
//	`cart_id` BIGINT(20) NOT NULL,
//	`category_id` BIGINT(20) NOT NULL,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String gdsName;

	private String cateCode;

	private BigDecimal gdsPrice;

	private int gdsStock;
	
	private String gdsDesc;
	
	@CreatedDate
	private LocalDateTime regDate;
	
	@LastModifiedDate
	private LocalDateTime upDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods")
	private List<OrderDetail> orderDetailList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods", cascade = CascadeType.REMOVE)
	private List<GoodsImage> goodsImageList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods", cascade = CascadeType.REMOVE)
	private List<Comment> commentList;
	
	@ManyToOne
	private Category category;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goods", cascade = CascadeType.REMOVE)
	private List<Cart> cartList;
}
