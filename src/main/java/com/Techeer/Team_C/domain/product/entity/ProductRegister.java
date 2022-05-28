package com.Techeer.Team_C.domain.product.entity;

import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "productId"})) //unique로 묶
public class ProductRegister extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productRegisterId;

    private Integer desired_price;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @Builder
    public ProductRegister(User user, Product product, Integer desired_price) {
        this.user = user;
        this.product = product;
        this.desired_price = desired_price;
    }

}
