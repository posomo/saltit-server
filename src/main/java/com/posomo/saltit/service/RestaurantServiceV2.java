package com.posomo.saltit.service;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.repository.RestaurantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceV2 implements RestaurantService {
	private final RestaurantRepository restaurantRepository;

	@Override
	public RestaurantSummaryResponse getRestaurantSummaries(RestaurantFilterRequest filterRequest) {
		Slice<RestaurantSummary> restaurantByFilterRequest = restaurantRepository.findRestaurantByFilterRequest(
			filterRequest);
		return RestaurantSummaryResponse.ofSummary(restaurantByFilterRequest);
	}

	@Override
	public RestaurantDetailResponse getRestaurantDetail(long restaurantId) {
		Restaurant restaurantWithMenus = restaurantRepository.findByIdWithMenus(restaurantId).orElseThrow(NoRecordException::new);
		Restaurant restaurantWithCategories = restaurantRepository.findByIdWithCategories(restaurantId).orElseThrow(NoRecordException::new);
		restaurantWithMenus.setCategories(restaurantWithCategories.getCategories());
		return RestaurantDetailResponse.of(restaurantWithMenus);
	}
}
