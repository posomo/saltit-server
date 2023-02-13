package com.posomo.saltit.domain.restaurant.entity.core.entity;


import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {
    @Id
    protected UUID id;

    protected BaseEntity(UUID id) {
        this.id = id;
    }
}
