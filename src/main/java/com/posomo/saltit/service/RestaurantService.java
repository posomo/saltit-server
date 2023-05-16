package com.posomo.saltit.service;

import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;

public interface RestaurantService {
	RestaurantSummaryResponse searchRestaurantSummaries(RestaurantFilterRequest filterRequest);

	RestaurantDetailResponse getRestaurantDetail(long restaurantId);
}
