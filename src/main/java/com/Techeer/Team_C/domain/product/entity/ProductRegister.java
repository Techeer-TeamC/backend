package com.Techeer.Team_C.domain.product.entity;

import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;

import javax.persistence.Convert;
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
import javax.validation.constraints.NotNull;

import com.Techeer.Team_C.global.utils.dto.BooleanToYNConverter;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class ProductRegister extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productRegisterId;

    private int desiredPrice;

    private int minimumPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private boolean status;

    @Builder
    public ProductRegister(User user, Product product, int desiredPrice, int minimumPrice, boolean status) {
        this.user = user;
        this.product = product;
        this.desiredPrice = desiredPrice;
        this.minimumPrice = minimumPrice;
        this.status = status;
    }

    public void update(int desiredPrice, boolean status) {
        this.desiredPrice = desiredPrice;
        this.status = status;
    }

}
