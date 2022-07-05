package com.Techeer.Team_C.domain.product.entity;


import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import com.Techeer.Team_C.global.utils.dto.BooleanToYNConverter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    private String image;

    private String name;

    private String url;

    private Integer minimumPrice;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private boolean status;

    @OneToMany(mappedBy = "product")
    private List<ProductRegister> productRegister;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //영속화 설정
    private List<Mall> mallInfo = new ArrayList<>(); //제품에 대한 쇼핑몰 Info , ArrayList<>초기화로 null 오류 방지

    public void addMallInfo(Mall mall) {
        this.mallInfo.add(mall);
    }
}
