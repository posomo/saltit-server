package com.posomo.saltit.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import com.posomo.saltit.respository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

	@InjectMocks
	private RestaurantService restaurantService;

	@Mock
	private RestaurantRepository restaurantRepository;

	@Nested
	@DisplayName("식당 세부 정보 조회 서비스(Entity -> DTO)")
	class getRestaurantDetail {
		@Test
		@DisplayName("정상흐름")
		void ok() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			menus.add(new RestaurantMenu(1L, null, "mainMenu1", 10000, null, null, true));
			menus.add(new RestaurantMenu(2L, null, "mainMenu2", 8000, null, null, true));
			menus.add(new RestaurantMenu(3L, null, "mainMenu3", 11000, null, null, true));
			menus.add(new RestaurantMenu(4L, null, "sideMenu1", 2000, null, null, false));
			menus.add(new RestaurantMenu(5L, null, "sideMenu2", 1000, null, null, false));
			Restaurant restaurant = Restaurant.create(1L, null, "테스트 식당", null, 100, null, null, menus, null, null, null);
			when(restaurantRepository.findByIdWithMenus(1L)).thenReturn(Optional.of(restaurant));

			//when
			RestaurantDetailResponse restaurantDetail = restaurantService.getRestaurantDetail(1L);

			// //then
			assertThat(restaurantDetail.getRating()).isEqualTo(100);
			assertThat(restaurantDetail.getName()).isEqualTo("테스트 식당");
			assertThat(restaurantDetail.getTotalMenuCount()).isEqualTo(5);
			assertThat(restaurantDetail.getMain().getCount()).isEqualTo(3);
			assertThat(restaurantDetail.getSide().getCount()).isEqualTo(2);

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
		}

		@Test
		@DisplayName("해당 가게에 메뉴가 0개일 때")
		void edge1() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			Restaurant restaurant = Restaurant.create(1L, null, "테스트 식당", null, 100, null, null, menus, null, null,
				null);
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
}
