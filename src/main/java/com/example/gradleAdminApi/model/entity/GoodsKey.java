package com.example.gradleAdminApi.model.entity;

import com.example.gradleAdminApi.model.enumclass.OrderStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "orderDetail"})
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class GoodsKey {
//	  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	  `key` VARCHAR(100) NOT NULL,
//	  `goods_name` VARCHAR(50) NOT NULL,
//	  `status` VARCHAR(20) NOT NULL,
//	  `reg_date` DATETIME NOT NULL,
//	  `up_date` DATETIME NOT NULL,
//	  `user_id` BIGINT(20) NOT NULL,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regKey;

    private String goodsName;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    private LocalDateTime regDate;

    private LocalDateTime upDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private OrderDetail orderDetail;
}
