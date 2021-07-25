package com.example.gradleAdminApi.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.example.gradleAdminApi.model.enumclass.UserAccess;
import com.example.gradleAdminApi.model.enumclass.UserStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@ToString(exclude = {"orderList", "replyList", "cartList", "goodsKeyList"})
@Entity
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class User {
//	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	`user_email` VARCHAR(50) NOT NULL,
//	`passwd` VARCHAR(100) NOT NULL,
//	`user_name` VARCHAR(30) NOT NULL,
//	`user_sur_name` VARCHAR(30) NOT NULL,
//	`birthday` VARCHAR(30) NULL,
//	`post_code` VARCHAR(50) NULL,
//	`user_addr1` VARCHAR(30) NULL,
//	`user_addr2` VARCHAR(50) NULL,
//	`user_addr3` VARCHAR(50) NULL,
//	`phone_num` VARCHAR(20) NULL,
//	`status` VARCHAR(20) NOT NULL,
//	`access` TINYINT NOT NULL DEFAULT 0,
//	`last_login_at` DATETIME NULL,
//	`reg_date` DATETIME NOT NULL,
//	`up_date` DATETIME NULL,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String userEmail;
	
	private String passwd;
	
	private String userName;
	
	private String userSurname;
	
	private String birthday;
	
	private String postCode;
	
	private String userAddr1;
	
	private String userAddr2;
	
	private String userAddr3;
	
	private String phoneNum;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Enumerated(EnumType.STRING)
	private UserAccess access;
	
	private LocalDateTime lastLoginAt;
	
	@CreatedDate
	private LocalDateTime regDate;

	private LocalDateTime upDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<OrderGroup> orderList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Comment> replyList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Cart> cartList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<GoodsKey> goodsKeyList;
}
