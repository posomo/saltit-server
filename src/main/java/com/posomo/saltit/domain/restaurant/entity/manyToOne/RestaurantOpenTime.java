package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("OPEN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantOpenTime extends RestaurantTime{

    @Builder
    public RestaurantOpenTime(UUID id, Restaurant restaurant,
                              LocalTime timeFrom, LocalTime timeTo,
                              List<RestaurantTimeDay> days) {
        super(id, restaurant, timeFrom, timeTo, days);
    }

    public RestaurantOpenTime(UUID id) {
        super(id);
    }
}