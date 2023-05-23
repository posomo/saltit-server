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
import com.posomo.saltit.domain.restaurant.entity.Category;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantCategory;
import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import com.posomo.saltit.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceV1Test {

	private RestaurantService restaurantService;

	@Mock
	private RestaurantRepository restaurantRepository;

	@BeforeEach
	public void initTestObject() {
		restaurantService = new RestaurantServiceV1(restaurantRepository);
	}

	@Nested
	@DisplayName("식당 세부 정보 조회 서비스(Entity -> DTO)")
	class getRestaurantDetail {
		@Test
		@DisplayName("정상흐름")
		void ok() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			menus.add(RestaurantMenu.builder().id(1L).name("mainMenu1").price(10000).mainMenu(true).build());
			menus.add(RestaurantMenu.builder().id(2L).name("mainMenu2").price(8000).mainMenu(true).build());
			menus.add(RestaurantMenu.builder().id(3L).name("mainMenu3").price(11000).mainMenu(true).build());
			menus.add(RestaurantMenu.builder().id(4L).name("sideMenu1").price(2000).mainMenu(false).build());
			menus.add(RestaurantMenu.builder().id(5L).name("sideMenu2").price(1000).mainMenu(false).build());
			Restaurant restaurant = Restaurant.builder().id(1L).name("테스트 식당").score(100).menus(menus).build();
			restaurant.setPhone("phone");
			restaurant.setLocation(new RestaurantLocation(1L, restaurant, "address", null));
			restaurant.setCategories(Arrays.asList(
				new RestaurantCategory(1L, restaurant, new Category(1L, "한식")),
				new RestaurantCategory(2L, restaurant, new Category(1L, "양식"))
			));
			when(restaurantRepository.findByIdWithMenus(1L)).thenReturn(Optional.of(restaurant));

			//when
			RestaurantDetailResponse restaurantDetail = restaurantService.getRestaurantDetail(1L);

			// //then
			assertThat(restaurantDetail.getRating()).isEqualTo(100);
			assertThat(restaurantDetail.getName()).isEqualTo("테스트 식당");
			assertThat(restaurantDetail.getTotalMenuCount()).isEqualTo(5);
			assertThat(restaurantDetail.getMain().getCount()).isEqualTo(3);
			assertThat(restaurantDetail.getSide().getCount()).isEqualTo(2);
			assertThat(restaurantDetail.getPhone()).isEqualTo("phone");
			assertThat(restaurantDetail.getAddress()).isEqualTo("address");
			assertThat(restaurantDetail.getCategories()).hasSize(2);


			RestaurantDetailResponse.Classification.Menu mainMenu1 = restaurantDetail.getMain().getMenus()
				.stream()
				.filter(menu -> menu.getId() == 1L)
				.findAny()
				.get();
			assertThat(mainMenu1.getId()).isEqualTo(1L);
			assertThat(mainMenu1.getName()).isEqualTo("mainMenu1");
			assertThat(mainMenu1.getPrice()).isEqualTo(10000);

			RestaurantDetailResponse.Classification.Menu sideMenu1 = restaurantDetail.getSide().getMenus()
				.stream()
				.filter(menu -> menu.getId() == 4L)
				.findAny()
				.get();
			assertThat(sideMenu1.getId()).isEqualTo(4L);
			assertThat(sideMenu1.getName()).isEqualTo("sideMenu1");
			assertThat(sideMenu1.getPrice()).isEqualTo(2000);

			assertThat(restaurantDetail.getCategories().stream().filter(category -> category.equals("한식")).toList()).hasSize(1);
			assertThat(restaurantDetail.getCategories().stream().filter(category -> category.equals("양식")).toList()).hasSize(1);

		}

		@Test
		@DisplayName("해당 가게에 메뉴가 0개일 때")
		void edge1() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			Restaurant restaurant = Restaurant.builder().id(1L).name("테스트 식당").score(100).menus(menus).build();
			restaurant.setPhone("phone");
			restaurant.setLocation(new RestaurantLocation(1L, restaurant, "address", null));
			restaurant.setCategories(Arrays.asList(
				new RestaurantCategory(1L, restaurant, new Category(1L, "한식")),
				new RestaurantCategory(2L, restaurant, new Category(1L, "양식"))
			));
			when(restaurantRepository.findByIdWithMenus(1L)).thenReturn(Optional.of(restaurant));

			//when
			RestaurantDetailResponse restaurantDetail = restaurantService.getRestaurantDetail(1L);

			// //then
			assertThat(restaurantDetail.getRating()).isEqualTo(100);
			assertThat(restaurantDetail.getName()).isEqualTo("테스트 식당");
			assertThat(restaurantDetail.getTotalMenuCount()).isZero();
			assertThat(restaurantDetail.getMain().getCount()).isZero();
			assertThat(restaurantDetail.getSide().getCount()).isZero();
		}

		@Test
		@DisplayName("식당이 없을 때")
		void edge2() {
			//when
			when(restaurantRepository.findByIdWithMenus(1L)).thenReturn(Optional.empty());

			// //then
			assertThrows(NoRecordException.class, () -> restaurantService.getRestaurantDetail(1L));
		}
	}

	@Nested
	@DisplayName("식당 필터 검색 서비스")
	class searchRestaurantSummaries {
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
