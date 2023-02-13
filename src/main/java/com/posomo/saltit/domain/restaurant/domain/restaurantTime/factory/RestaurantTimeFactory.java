package com.posomo.saltit.domain.restaurant.domain.restaurantTime.factory;

import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeDto;
import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeType;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.enums.Day;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTimeDay;
import java.util.List;
import java.util.UUID;

public abstract class RestaurantTimeFactory {

    public RestaurantTime createTime(Restaurant restaurant, RestaurantTimeDto timeDto) {
        UUID timeId = UUID.randomUUID();
        RestaurantTime timeFk = new RestaurantTime(timeId);
        List<RestaurantTimeDay> days = Day.toRestaurantTimeDayList(timeFk,
                timeDto.getDays());
        return build(timeId,restaurant, days, timeDto);
    }
    static public RestaurantTimeFactory of(RestaurantTimeType type){
        if(type == RestaurantTimeType.OPEN)return new RestaurantOpenTimeFactory();
        if(type == RestaurantTimeType.LAST_ORDER)return new RestaurantLastOrderTimeFactory();
        if(type == RestaurantTimeType.BREAK)return new RestaurantBreakTimeFactory();
        return null;
    }
    abstract protected RestaurantTime build(UUID id, Restaurant restaurant, List<RestaurantTimeDay> days, RestaurantTimeDto timeDto);
}
