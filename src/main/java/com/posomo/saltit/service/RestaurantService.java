package com.posomo.saltit.service;

import org.springframework.data.domain.Slice;

import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;

public interface RestaurantService {
	Slice<RestaurantSummary> getRestaurantSummaries(RestaurantFilterRequest filterRequest);

	public RestaurantDetailResponse getRestaurantDetail(long restaurantId);
}
