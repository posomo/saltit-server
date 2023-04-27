package com.posomo.saltit.service;
import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.respository.RestaurantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceV1 implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    public RestaurantSummaryResponse getRestaurantSummaries(RestaurantFilterRequest filterRequest){
        Slice<Object[]> resultObjects = restaurantRepository.findRestaurantByFilter(
            filterRequest.getMaxPrice(),
            filterRequest.getFoodTypeName(),
            filterRequest.computeMySqlPoint(),
            filterRequest.getMaxDistance(),
            filterRequest.createPageRequest()
        );
        return RestaurantSummaryResponse.of(resultObjects);
    }

    public RestaurantDetailResponse getRestaurantDetail(long restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdWithMenus(restaurantId).orElseThrow(NoRecordException::new);
        return RestaurantDetailResponse.of(restaurant);
    }
}
