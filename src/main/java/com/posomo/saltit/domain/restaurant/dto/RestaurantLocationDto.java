package com.posomo.saltit.domain.restaurant.dto;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.oneToOne.RestaurantLocation;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class RestaurantLocationDto {
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    public static RestaurantLocation toEntity(Restaurant restaurant, RestaurantLocationDto restaurantLocationDtoDto){
        return RestaurantLocation.builder()
                .restaurant(restaurant)
                .roadAddress(restaurantLocationDtoDto.roadAddress)
                .latitude(restaurantLocationDtoDto.latitude)
                .longitude(restaurantLocationDtoDto.longitude)
                .build();
    }
}

