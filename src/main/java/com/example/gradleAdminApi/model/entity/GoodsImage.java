package com.example.gradleAdminApi.model.entity;

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
@ToString(exclude = {"goods"})
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class GoodsImage {
//	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	`img_name` VARCHAR(200) NOT NULL,
//	`ori_name` VARCHAR(50) NOT NULL,
//	`gds_img` VARCHAR(200) NULL,
//	`gds_thumb_img` VARCHAR(200) NULL,
//	`img_size` DECIMAL(12,4) NULL,
//	`img_flg` TINYINT NOT NULL DEFAULT 1,
//	`reg_date` DATETIME NULL,
//	`up_date` DATETIME NULL,
//	`goods_id` BIGINT(20) NOT NULL,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imgName;
	
	private String oriName;
	
	private String gdsImg;
	
	private String gdsThumbImg;
	
	private Long imgSize;
	
	private int imgFlg;
	
	@CreatedDate
	private LocalDateTime regDate;
	
	@LastModifiedDate
	private LocalDateTime upDate;
	
	@ManyToOne
	private Goods goods;
}
