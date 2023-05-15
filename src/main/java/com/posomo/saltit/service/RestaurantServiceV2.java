package com.posomo.saltit.service;

import org.springframework.stereotype.Service;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSearchCondition;
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

		RestaurantSearchCondition searchDto = new RestaurantSearchCondition(filterRequest);

		if (searchDto.getSearch() == null) {
			return RestaurantSummaryResponse.ofSummary(
				restaurantRepository.searchRestaurant(searchDto));
		}
		return RestaurantSummaryResponse.ofSummary(
			restaurantRepository.searchRestaurantContainStringSearch(searchDto));
	}

	@Override
	public RestaurantDetailResponse getRestaurantDetail(long restaurantId) {
		Restaurant restaurant = restaurantRepository.findByIdWithMenus(restaurantId)
			.orElseThrow(NoRecordException::new);
		return RestaurantDetailResponse.of(restaurant);
	}
}
