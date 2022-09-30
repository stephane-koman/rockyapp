package com.rockyapp.rockyappbackend.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractSocleEntity implements SocleEntity {

    private static final long serialVersionUID = -7740347057780248917L;

    @Column(name = "is_active")
    private int active = 1;

    @Column(name = "is_delete")
    private int delete = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
