package com.posomo.saltit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;

@RequestMapping("/api/v1")
public interface RestaurantController {
	@PostMapping("/home/restaurant-summary")
	RestaurantSummaryResponse getRestaurantSummaries(@RequestBody RestaurantFilterRequest filterRequest);

	@GetMapping("/restaurant/detail/{restaurantId}")
	RestaurantDetailResponse getRestaurantDetail(@PathVariable("restaurantId") Integer restaurantId);
}
