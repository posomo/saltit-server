package com.posomo.saltit.domain.restaurant.dto;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.oneToOne.RestaurantInformation;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class RestaurantInformationDto {
    BigDecimal rating;

    public static RestaurantInformation toEntity(Restaurant restaurant, RestaurantInformationDto restaurantInformationDto){
        return RestaurantInformation.builder()
                .restaurant(restaurant)
                .rating(restaurantInformationDto.rating)
                .build();
    }
}
