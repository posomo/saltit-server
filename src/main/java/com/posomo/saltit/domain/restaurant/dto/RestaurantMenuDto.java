package com.posomo.saltit.domain.restaurant.dto;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantMenu;
import lombok.Data;

@Data
public class RestaurantMenuDto {
    Integer price;
    String name;
    Boolean isMainMenu;
    String pictureUrl;

    public static RestaurantMenu toEntity(Restaurant restaurant, RestaurantMenuDto restaurantMenuDto){
        return RestaurantMenu.builder()
                .restaurant(restaurant)
                .price(restaurantMenuDto.price)
                .pictureUrl(restaurantMenuDto.pictureUrl)
                .name(restaurantMenuDto.name)
                .isMainMenu(restaurantMenuDto.isMainMenu)
                .build();
    }
}
