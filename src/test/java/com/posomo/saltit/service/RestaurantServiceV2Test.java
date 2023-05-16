package com.posomo.saltit.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;
import com.posomo.saltit.domain.restaurant.entity.Category;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantCategory;
import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import com.posomo.saltit.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceV2Test {

	private RestaurantService restaurantService;

	@Mock
	private RestaurantRepository restaurantRepository;

	@BeforeEach
	public void initTestObject() {
		restaurantService = new RestaurantServiceV2(restaurantRepository);
	}

	@Nested
	@DisplayName("식당 필터 검색 서비스")
	class getRestaurantDetail {
		private Slice<RestaurantSummary> someRestaurantData() {
			return new SliceImpl<>(new ArrayList<>(), PageRequest.of(0, 1), false);
		}

		@Test
		@DisplayName("식당 필터 검색 서비스 : StringSearch 포함")
		void ok() {
			// Given
			RestaurantFilterRequest mockFilterRequest = mock(RestaurantFilterRequest.class);
			RestaurantFilterRequest.Options mockOptions = mock(RestaurantFilterRequest.Options.class);
			when(mockFilterRequest.getOptions()).thenReturn(mockOptions);

			when(restaurantRepository.searchRestaurant(any())).thenReturn(someRestaurantData());
			// When
			restaurantService.searchRestaurantSummaries(mockFilterRequest);

			// Then
			verify(restaurantRepository).searchRestaurant(any());
		}

		@Test
		@DisplayName("식당 필터 검색 서비스 : StringSearch 포함하지 않는 경우")
		void edge1() {
			//given
			RestaurantFilterRequest mockFilterRequest = mock(RestaurantFilterRequest.class);
			RestaurantFilterRequest.Options mockOptions = mock(RestaurantFilterRequest.Options.class);
			when(mockOptions.getSearch()).thenReturn("테스트");
			when(mockFilterRequest.getOptions()).thenReturn(mockOptions);

			when(restaurantRepository.searchRestaurantContainStringSearch(any())).thenReturn(someRestaurantData());
			// When
			restaurantService.searchRestaurantSummaries(mockFilterRequest);

			// Then
			verify(restaurantRepository).searchRestaurantContainStringSearch(any());
		}

	}
}
