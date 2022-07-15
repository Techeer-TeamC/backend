package com.Techeer.Team_C.domain.product.entity;

import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productHistoryId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int minimumPrice;

    private String mallName;
}
