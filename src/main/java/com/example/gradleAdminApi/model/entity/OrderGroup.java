package com.example.gradleAdminApi.model.entity;

import com.example.gradleAdminApi.model.enumclass.OrderStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user"})
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderGroup {
//	`id` BIGINT(20) NOT NULL,
//	`order_num` VARCHAR(100) NOT NULL,
//	`order_rcv` VARCHAR(50) NOT NULL,
//	`post_code` VARCHAR(30) NOT NULL,
//	`user_addr1` VARCHAR(30) NOT NULL,
//	`user_addr2` VARCHAR(50) NOT NULL,
//	`user_addr3` VARCHAR(50) NOT NULL,
//	`phone_num` VARCHAR(20) NOT NULL,
//	`total_price` DECIMAL(12,4) NOT NULL,
//	`total_quantity` INT NOT NULL,
//	`payment_type` VARCHAR(50) NOT NULL,
//	`order_status` VARCHAR(20) NOT NULL,
//	`order_date` DATETIME NOT NULL,
//	`user_id` BIGINT(20) NOT NULL,
//	`cart_id` BIGINT(20) NOT NULL,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String orderNum;

	private String orderRcp;

	private String postCode;

	private String userAddr1;

	private String userAddr2;

	private String userAddr3;

	private String phoneNum;

	private BigDecimal totalPrice;

	private int totalQuantity;

	private String paymentType;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@CreatedDate
	private LocalDateTime orderDate;

	@LastModifiedDate
	private LocalDateTime upDate;

	@ManyToOne
	private User user;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderGroup", cascade = CascadeType.REMOVE)
	private List<OrderDetail> orderDetailList;
}
