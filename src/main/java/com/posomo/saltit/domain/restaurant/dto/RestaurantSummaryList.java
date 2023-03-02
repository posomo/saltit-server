package com.posomo.saltit.domain.restaurant.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantSummaryList {
    List<RestaurantSummary> restaurantSummaries;
}
