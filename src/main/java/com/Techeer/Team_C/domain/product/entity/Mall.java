package com.Techeer.Team_C.domain.product.entity;

import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity

public class Mall extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mallId;

    private String link;           // 쇼핑몰 링크
    private String price;          // 쇼핑몰 제품 가격
    private Integer delivery;      // 무료배송 여부 (0 -> 무료배송)
    private String interestFree;   // 할부 기간
    private String paymentOption;  // 최저가 or 현금 가능 여부

    @ManyToOne(fetch = FetchType.LAZY) //N+1문제 회피
    @JoinColumn(name = "mall_id")
    private Product product;

}
