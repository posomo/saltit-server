package com.posomo.saltit.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;
import com.posomo.saltit.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@Log4j2
public class RestaurantControllerV1 implements RestaurantControllerV1Swagger, RestaurantController {
	private final RestaurantService restaurantService;

	@Override
	public RestaurantSummaryResponse getRestaurantSummaries(@Validated @RequestBody RestaurantFilterRequest filterRequest) {
		return restaurantService.getRestaurantSummaries(filterRequest);
	}

	@Override
	public RestaurantDetailResponse getRestaurantDetail(@PathVariable("restaurantId") Integer restaurantId) {
		return restaurantService.getRestaurantDetail(restaurantId);
	}
}
