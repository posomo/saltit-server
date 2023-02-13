package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import com.posomo.saltit.domain.restaurant.entity.core.enums.Day;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTimeDay extends BaseEntity {

    @JoinColumn(name="restaurant_time_id", referencedColumnName = "id")
    @ManyToOne
    private RestaurantTime time;
    @Enumerated(value = EnumType.STRING)
    private Day day;

    @Builder
    public RestaurantTimeDay(UUID id, RestaurantTime time, Day day) {
        super(id);
        this.time = time;
        this.day = day;
    }
}
