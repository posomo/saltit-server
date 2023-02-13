package com.posomo.saltit.domain.restaurant.domain.restaurantTime.factory;

import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeDto;
import com.posomo.saltit.domain.restaurant.dto.RestaurantTimeType;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;

public interface RestaurantTimeFactory {
    RestaurantTime createTime(Restaurant restaurant, RestaurantTimeDto timeDto);
    static RestaurantTimeFactory of(RestaurantTimeType type){
        if(type == RestaurantTimeType.OPEN)return new RestaurantOpenTimeFactory();
        if(type == RestaurantTimeType.LAST_ORDER)return new RestaurantLastOrderTimeFactory();
        if(type == RestaurantTimeType.BREAK)return new RestaurantBreakTimeFactory();
        return null;
    }
}
