package com.posomo.saltit.domain.restaurant.entity.core.entity;


import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @Id
    protected UUID id;
}
