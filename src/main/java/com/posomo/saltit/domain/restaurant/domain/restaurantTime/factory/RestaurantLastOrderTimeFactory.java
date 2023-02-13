package com.posomo.saltit.domain.restaurant.domain.restaurantTime.factory;

import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeDto;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantLastOrderTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTimeDay;
import java.util.List;
import java.util.UUID;

public class RestaurantLastOrderTimeFactory extends RestaurantTimeFactory {


    @Override
    protected RestaurantTime build(UUID id, Restaurant restaurant, List<RestaurantTimeDay> days,
                                   RestaurantTimeDto timeDto) {
        return RestaurantLastOrderTime.builder()
                .id(id)
                .timeTo(timeDto.getTimeTo())
                .timeFrom(timeDto.getTimeFrom())
                .restaurant(restaurant)
                .days(days)
                .build();
    }
}
