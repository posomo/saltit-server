package com.posomo.saltit.repository;

import org.springframework.data.domain.Slice;

import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;

public interface RestaurantRepositoryCustom {

	Slice<RestaurantSummary> findRestaurantByFilterRequest(RestaurantFilterRequest restaurantFilterRequest);
}
