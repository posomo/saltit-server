package com.posomo.saltit.domain.restaurant.dto;


import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantMenu;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.oneToOne.RestaurantInformation;
import com.posomo.saltit.domain.restaurant.entity.oneToOne.RestaurantLocation;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class RestaurantDto {
    String rid;
    String name;
    String titleImageUrl;
    RestaurantLocationDto location;
    RestaurantInformationDto information;
    List<RestaurantMenuDto> menus;
    List<RestaurantTimeDto> times;

    public static Restaurant toEntity(RestaurantDto restaurantDto){
        UUID id = UUID.randomUUID();
        Restaurant fk = Restaurant.builder().id(id).build();
        RestaurantInformation information = RestaurantInformationDto.toEntity(fk,restaurantDto.getInformation());
        RestaurantLocation location = RestaurantLocationDto.toEntity(fk, restaurantDto.getLocation());
        List<RestaurantTime> times = restaurantDto.getTimes().stream().map(timeDto ->RestaurantTimeDto.toEntity(fk, timeDto)).toList();
        List<RestaurantMenu> menus = restaurantDto.getMenus().stream().map(menu->RestaurantMenuDto.toEntity(fk, menu)).toList();
        return Restaurant.builder()
                .id(id)
                .rid(restaurantDto.rid)
                .name(restaurantDto.name)
                .titleImageUrl(restaurantDto.titleImageUrl)
                .location(location)
                .information(information)
                .times(times)
                .menus(menus)
                .build();
    }

}