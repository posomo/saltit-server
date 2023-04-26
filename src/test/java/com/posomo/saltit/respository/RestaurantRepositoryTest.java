package com.posomo.saltit.respository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RestaurantRepositoryTest {

	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private EntityManager entityManager;

	@Nested
	@DisplayName("Restaurant 데이터를 Menus 정보와 함께 조회(n+1 해결)")
	class findByIdWithMenus {
		@Test
		@DisplayName("정상흐름")
		void ok() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			Restaurant restaurant = Restaurant.create(1L, null, "테스트 가게", null, 100, null, null, menus, null, null, null);
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));

			//when
			Restaurant savedRestaurant = restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			//then
			Restaurant findRestaurant = restaurantRepository.findByIdWithMenus(savedRestaurant.getId()).get();
			assertThat(findRestaurant.getId()).isEqualTo(savedRestaurant.getId());
			assertThat(findRestaurant.getName()).isEqualTo(savedRestaurant.getName());
			assertThat(findRestaurant.getScore()).isEqualTo(savedRestaurant.getScore());
			assertThat(findRestaurant.getMenus()).hasSize(menus.size());
		}
		@Test
		@DisplayName("menu join fetch 작동 테스트")
		void nPlusOneCheck() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			Restaurant restaurant = Restaurant.create(1L, null, "테스트 가게", null, 100, null, null, menus, null, null, null);
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));
			menus.add(new RestaurantMenu(null, restaurant, null, null, null, null, true));

			//when
			Restaurant savedRestaurant = restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();
			Restaurant findRestaurantLazy = restaurantRepository.findByIdWithMenus(savedRestaurant.getId()).get();
			entityManager.clear();

			//then
			assertDoesNotThrow(() -> findRestaurantLazy.getMenus().get(0));
		}

		@Test
		@DisplayName("해당 가게에 메뉴가 0개일 때")
		void edge1() {
			//given
			List<RestaurantMenu> menus = new ArrayList<>();
			Restaurant restaurant = Restaurant.create(1L, null, "테스트 가게", null, 100, null, null, menus, null, null, null);

			//when
			Restaurant savedRestaurant = restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			//then
			Restaurant findRestaurant = restaurantRepository.findByIdWithMenus(savedRestaurant.getId()).get();
			assertThat(findRestaurant.getId()).isEqualTo(savedRestaurant.getId());
			assertThat(findRestaurant.getName()).isEqualTo(savedRestaurant.getName());
			assertThat(findRestaurant.getScore()).isEqualTo(savedRestaurant.getScore());
			assertThat(findRestaurant.getMenus()).isEmpty();
		}

		@Test
		@DisplayName("식당이 없을 때")
		void edge2() {
			//given
			//when
			//then
			Optional<Restaurant> findRestaurant = restaurantRepository.findByIdWithMenus(1L);
			assertThat(findRestaurant).isEmpty();
		}
	}
}
