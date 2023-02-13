package com.posomo.saltit.domain.restaurant.domain.restaurantTime.factory;

import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeDto;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.enums.Day;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantOpenTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTimeDay;
import java.util.List;
import java.util.UUID;

public class RestaurantOpenTimeFactory implements RestaurantTimeFactory {

    @Override
    public RestaurantTime createTime(Restaurant restaurant, RestaurantTimeDto timeDto) {
        UUID timeId = UUID.randomUUID();
        RestaurantTime timeFk = new RestaurantTime(timeId);
        List<RestaurantTimeDay> days = Day.toRestaurantTimeDayList(timeFk,
                timeDto.getDays());
        return RestaurantOpenTime.builder()
                .id(timeId)
                .timeTo(timeDto.getTimeTo())
                .timeFrom(timeDto.getTimeFrom())
                .restaurant(restaurant)
                .days(days)
                .build();
    }
}
