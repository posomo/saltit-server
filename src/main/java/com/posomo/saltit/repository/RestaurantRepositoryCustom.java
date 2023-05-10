package com.posomo.saltit.repository;

import java.util.List;

import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;

public interface RestaurantRepositoryCustom {

	List<RestaurantSummary> findRestaurantBy(RestaurantFilterRequest restaurantFilterRequest);
}
