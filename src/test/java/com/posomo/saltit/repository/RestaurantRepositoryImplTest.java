package com.posomo.saltit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.posomo.saltit.TestConfig;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.FoodType;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
class RestaurantRepositoryImplTest {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RestaurantLocationRepository restaurantLocationRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@DisplayName("QueryDSL을 사용하여 반경 거리에 있는 목록 조회")
	@Test
	void 필터로_반경목록_가져오기_성공() {

		// Given
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		List<RestaurantMenu> menus = new ArrayList<>();
		String typeName = "한식";

		FoodType foodType = FoodType.builder()
			.name(typeName)
			.build();

		Restaurant restaurant = Restaurant.builder()
			.name("테스트 가게")
			.score(100)
			.menus(menus)
			.foodType(foodType)
			.build();

		RestaurantLocation location = RestaurantLocation.builder()
			.location(factory.createPoint(new Coordinate(127.0521, 37.5033)))
			.restaurant(restaurant)
			.build();

		menus.add(RestaurantMenu.builder().price(10000).restaurant(restaurant).build());
		menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
		menus.add(RestaurantMenu.builder().price(5000).restaurant(restaurant).orderNumber(1).build());

		// when
		restaurantLocationRepository.save(location);
		restaurant.setLocation(location);
		restaurantRepository.save(restaurant);
		entityManager.flush();
		entityManager.clear();

		List<RestaurantSummary> restaurantSummaries = restaurantRepository.findRestaurantByFilterRequest(
			new RestaurantFilterRequest(typeName, 10.0, 1000000, 0, 100,
				location.getLocation().getX(), location.getLocation().getY())
		).getContent();

		// then
		assertTrue(restaurantSummaries
			.stream()
			.map(RestaurantSummary::getRestaurantId)
			.toList()
			.contains(restaurant.getId()));
	}

	@DisplayName("QueryDSL을 사용하여 반경 거리에 없는 목록 조회")
	@Test
	void 필터로_반경목록_가져오기_실패_거리() {

		// Given
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		List<RestaurantMenu> menus = new ArrayList<>();
		String typeName = "한식";

		FoodType foodType = FoodType.builder()
			.name(typeName)
			.build();

		Restaurant restaurant = Restaurant.builder()
			.name("테스트 가게")
			.score(100)
			.menus(menus)
			.foodType(foodType)
			.build();

		RestaurantLocation location = RestaurantLocation.builder()
			.location(factory.createPoint(new Coordinate(127.0521, 37.5033)))
			.restaurant(restaurant)
			.build();

		menus.add(RestaurantMenu.builder().price(10000).restaurant(restaurant).build());
		menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
		menus.add(RestaurantMenu.builder().price(5000).restaurant(restaurant).orderNumber(1).build());

		// when
		restaurantLocationRepository.save(location);
		restaurant.setLocation(location);
		restaurantRepository.save(restaurant);
		entityManager.flush();
		entityManager.clear();

		List<RestaurantSummary> restaurantSummaries = restaurantRepository.findRestaurantByFilterRequest(
			new RestaurantFilterRequest(typeName, 10000.0, 1000000, 0, 100,
				10.0, 10.0)
		).getContent();

		// then
		assertFalse(restaurantSummaries
			.stream()
			.map(RestaurantSummary::getRestaurantId)
			.toList()
			.contains(restaurant.getId()));
	}

	@DisplayName("QueryDSL로 필터와 검색으로 값을 가져오기 성공")
	@Test
	void 필터와_검색으로_값을_가져오기_성공() {

		// Given
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		List<RestaurantMenu> menus = new ArrayList<>();
		String typeName = "한식";

		FoodType foodType = FoodType.builder()
			.name(typeName)
			.build();

		Restaurant restaurant = Restaurant.builder()
			.name("테스트 가게")
			.score(100)
			.menus(menus)
			.foodType(foodType)
			.build();

		RestaurantLocation location = RestaurantLocation.builder()
			.location(factory.createPoint(new Coordinate(127.0521, 37.5033)))
			.restaurant(restaurant)
			.build();

		menus.add(RestaurantMenu.builder().price(10000).restaurant(restaurant).name("테스트 음식").build());
		menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
		menus.add(RestaurantMenu.builder().price(5000).restaurant(restaurant).orderNumber(1).name("테스트 콜라").build());

		// when
		restaurantLocationRepository.save(location);
		restaurant.setLocation(location);
		restaurantRepository.save(restaurant);
		entityManager.flush();
		entityManager.clear();

		List<RestaurantSummary> restaurantSummaries = restaurantRepository.findRestaurantByFilterRequest(
			new RestaurantFilterRequest(typeName, 1000.0, 1000000, 0, 100,
				location.getLocation().getX(), location.getLocation().getY(), "테스트")
		).getContent();

		// then
		assertTrue(restaurantSummaries
			.stream()
			.map(RestaurantSummary::getRestaurantId)
			.toList()
			.contains(restaurant.getId()));
	}
}