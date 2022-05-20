package com.Techeer.Team_C.domain.product.entity;


import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import com.Techeer.Team_C.global.utils.dto.BooleanToYNConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private long product_id;

    private String product_image;

    private String name;

    private int origin_price;

    private int minimum_price;

    private String product_link;

    @Column(columnDefinition = "TEXT")  //글자 수 제한 없음
    private String product_detail;

    private String shipment;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private boolean activated;

    //private ProductRegisterId product_registerId;
}
