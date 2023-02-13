package com.posomo.saltit.domain.restaurant.dto;

import com.posomo.saltit.domain.restaurant.domain.restaurantTime.factory.RestaurantTimeFactory;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.enums.Day;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class RestaurantTimeDto {
    LocalTime timeTo;
    LocalTime timeFrom;
    RestaurantTimeType dtype;
    List<Day> days;

    static public RestaurantTime toEntity(Restaurant restaurant, RestaurantTimeDto timeDto){
        RestaurantTimeFactory factory = RestaurantTimeFactory.of(timeDto.getDtype());
        return factory.createTime(restaurant, timeDto);
    }
}
