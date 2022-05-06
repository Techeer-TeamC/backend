package com.Techeer.Team_C.domain.user.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_ROLES")
@Getter
@Setter
public class Roles implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRolesType roles;  // ADMIN, MEMBER
}

enum UserRolesType {
    ROLE_USER, ROLE_ADMIN
}
