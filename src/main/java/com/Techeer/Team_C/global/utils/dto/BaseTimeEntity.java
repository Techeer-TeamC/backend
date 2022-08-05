package com.Techeer.Team_C.global.utils.dto;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @PrePersist
    public void onPrePersist() {
//        String format = LocalDateTime.now()
//            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        this.createdDate = LocalDateTime.parse(format,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        this.modifiedDate = LocalDateTime.parse(format,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.createdDate = LocalDateTime.now().withNano(0);
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDate = LocalDateTime.now().withNano(0);
    }

}
