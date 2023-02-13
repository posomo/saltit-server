package com.posomo.saltit.domain.restaurant.domain.restaurantTime.factory;

import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeDto;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantBreakTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTimeDay;
import java.util.List;
import java.util.UUID;


public class RestaurantBreakTimeFactory extends AbstractRestaurantTimeFactory{

    @Override
    protected RestaurantTime build(UUID id, Restaurant restaurant, List<RestaurantTimeDay> days, RestaurantTimeDto timeDto) {
        return RestaurantBreakTime.builder()
                .id(id)
                .timeTo(timeDto.getTimeTo())
                .timeFrom(timeDto.getTimeFrom())
                .restaurant(restaurant)
                .days(days)
                .build();
    }
}
