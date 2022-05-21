package com.Techeer.Team_C.domain.product.entity;

import com.Techeer.Team_C.domain.user.entity.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long register_id;

    private Integer desired_price;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;


    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;


}
