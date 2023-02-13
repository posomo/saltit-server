package com.posomo.saltit.domain.restaurant.dto;

import java.util.List;
import lombok.Data;

@Data
public class RestaurantCreateManyDto {
    List<RestaurantDto> restaurants;
}
