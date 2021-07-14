package com.example.gradleAdminApi.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
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
@ToString(exclude = {"goodsList"})
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Category {
//	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	`cate_name` VARCHAR(50) NOT NULL,
//	`cate_code` VARCHAR(30) NOT NULL,
//	`cate_code_ref` VARCHAR(30) NULL,
//	`reg_date` DATETIME NULL,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cateName;
	
	@Column(unique = true)
	private String cateCode;

	private String cateCodeRef;
	
	@CreatedDate
	private LocalDateTime regDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private List<Goods> goodsList;
}
