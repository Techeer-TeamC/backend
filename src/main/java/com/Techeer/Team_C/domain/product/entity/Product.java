package com.Techeer.Team_C.domain.product.entity;


import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import com.Techeer.Team_C.global.utils.dto.BooleanToYNConverter;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    private String image;

    private String name;

    private int originPrice;

    private int minimumPrice;

    private String link;

    @Column(columnDefinition = "TEXT")  //글자 수 제한 없음
    private String detail;

    private String shipment;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private boolean status;

    @OneToMany(mappedBy = "product")
    private List<ProductRegister> productRegister;
}
