package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import com.posomo.saltit.domain.restaurant.entity.core.enums.Day;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RestaurantTimeDay extends BaseEntity {

    @JoinColumn(name="restaurant_break_time_id")
    @ManyToOne()
    private RestaurantTime time;
    @Enumerated(value = EnumType.STRING)
    private Day day;
}
