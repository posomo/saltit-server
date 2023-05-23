package com.posomo.saltit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import com.posomo.saltit.TestConfig;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSearchCondition;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.Category;
import com.posomo.saltit.domain.restaurant.entity.FoodType;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantCategory;
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

	@Autowired
	private CategoryRepository categoryRepository;

	@DisplayName("StringSearch (문자열 서칭)을 포함하지 않은 쿼리")
	@Nested
	class findByNotContainStringSearch {
		@DisplayName("필터에 맞게 값을 가져오기 : 성공")
		@Test
		void success1() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.maxDistance(10.0)
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurant(
				searchCondition).getContent();

			// then
			assertTrue(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("가격에 포함된 갯수에 맞게 값을 가져옴 : 성공")
		@Test
		void success2() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(1000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(1000).restaurant(restaurant).mainMenu(true).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.maxDistance(10.0)
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurant(
				searchCondition).getContent();

			// then
			assertEquals(3, (int)restaurantSummaries
				.stream()
				.map(RestaurantSummary::getMenuSize)
				.findAny()
				.get());
		}

		@DisplayName("가격에 포함되는 메뉴가 존재하지 않음 : 실패")
		@Test
		void fail1() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(10000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.maxDistance(10.0)
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurant(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("타입에 포함되는 가게가 존재하지 않음 : 실패")
		@Test
		void fail2() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.maxDistance(10.0)
				.foodTypeName("양식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurant(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("거리에 포함되는 가게가 존재하지 않음 : 실패")
		@Test
		void fail3() {

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
				.location(factory.createPoint(new Coordinate(13.0521, 38.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.maxDistance(10.0)
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurant(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("메인메뉴에 포함되는 메뉴가 존재하지 않음 : 실패")
		@Test
		void fail4() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(false).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(false)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.maxDistance(10.0)
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurant(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}
	}

	@PersistenceContext
	private EntityManager entityManager;
	@DisplayName("StringSearch (문자열 서칭)을 포함한 쿼리")
	@Nested
	class findByStringSearch {

		@DisplayName("가격, 음식타입, 검색문자열(가게), 거리에 맞게 값을 가져오기 : 성공")
		@Test
		void success1() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("테스트")
				.maxDistance(10.0)
				// .sort("정확도순")
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			assertTrue(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("가격, 음식타입, 검색문자열(음식이름), 거리에 맞게 값을 가져오기 : 성공")
		@Test
		void success2() {

			// Given
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
			List<RestaurantMenu> menus = new ArrayList<>();
			String typeName = "한식";

			FoodType foodType = FoodType.builder()
				.name(typeName)
				.build();

			Restaurant restaurant = Restaurant.builder()
				.name("초밥회식집가게")
				.score(100)
				.menus(menus)
				.foodType(foodType)
				.build();

			RestaurantLocation location = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 음식")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("테스트")
				.maxDistance(10.0)
				// .sort("정확도순")
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			assertTrue(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("가격, 음식타입, 검색문자열(카테고리명), 거리에 맞게 값을 가져오기 : 성공")
		@Test
		void success3() {

			// Given
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
			List<RestaurantMenu> menus = new ArrayList<>();
			String typeName = "한식";

			FoodType foodType = FoodType.builder()
				.name(typeName)
				.build();

			Restaurant restaurant = Restaurant.builder()
				.name("초밥회식집가게")
				.score(100)
				.menus(menus)
				.foodType(foodType)
				.categories(new ArrayList<>())
				.build();

			RestaurantLocation location = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 음식")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("연어회초밥")
				.maxDistance(10.0)
				// .sort("정확도순")
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			Category category = Category.builder()
				.name("연어회초밥")
				.build();

			RestaurantCategory restaurantCategory = RestaurantCategory
				.builder()
				.restaurant(restaurant)
				.category(category)
				.build();

			// when
			categoryRepository.save(category);
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurant.getCategories().add(restaurantCategory);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			assertTrue(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("필터에 맞게 값을 가져오고 정확도순(가게이름 -> 카테고리명 -> 메뉴이름)으로 정렬 : 성공")
		@Test
		void success4() {

			// Given
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
			List<RestaurantMenu> menus1 = new ArrayList<>();
			List<RestaurantMenu> menus2 = new ArrayList<>();
			List<RestaurantMenu> menus3 = new ArrayList<>();

			FoodType foodType1 = FoodType.builder()
				.name("한식")
				.build();
			FoodType foodType2 = FoodType.builder()
				.name("중식")
				.build();
			FoodType foodType3 = FoodType.builder()
				.name("양식")
				.build();

			Restaurant restaurant1 = Restaurant.builder()
				.name("초밥회식집가게")
				.score(100)
				.menus(menus1)
				.foodType(foodType1)
				.categories(new ArrayList<>())
				.build();
			Restaurant restaurant2 = Restaurant.builder()
				.name("돼지국수가게")
				.score(50)
				.menus(menus2)
				.foodType(foodType2)
				.categories(new ArrayList<>())
				.build();
			Restaurant restaurant3 = Restaurant.builder()
				.name("돈까스가게")
				.score(30)
				.menus(menus3)
				.foodType(foodType3)
				.categories(new ArrayList<>())
				.build();

			RestaurantLocation location1 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant1)
				.build();
			RestaurantLocation location2 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant2)
				.build();
			RestaurantLocation location3 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant3)
				.build();

			menus1.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant1).mainMenu(true).name("테스트").build());
			menus1.add(RestaurantMenu.builder().price(100000).restaurant(restaurant1).build());
			menus1.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant1)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 음식")
				.build());

			menus2.add(RestaurantMenu.builder().price(10000).restaurant(restaurant2).mainMenu(true).name("초밥").build());
			menus2.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant2).mainMenu(true).name("돼지국밥").build());

			menus3.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant3).mainMenu(true).name("짜장면").build());
			menus3.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant3).mainMenu(true).name("아메리카노").build());

			Category category1 = Category.builder()
				.name("카테고리1")
				.build();
			RestaurantCategory restaurantCategory1 = RestaurantCategory
				.builder()
				.restaurant(restaurant1)
				.category(category1)
				.build();
			Category category2 = Category.builder()
				.name("카테고리2")
				.build();
			RestaurantCategory restaurantCategory2 = RestaurantCategory
				.builder()
				.restaurant(restaurant2)
				.category(category2)
				.build();
			Category category3 = Category.builder()
				.name("초밥")
				.build();
			RestaurantCategory restaurantCategory3 = RestaurantCategory
				.builder()
				.restaurant(restaurant3)
				.category(category3)
				.build();

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("초밥")
				.maxDistance(10.0)
				.sort("정확도순")
				// .foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();
			// when
			categoryRepository.saveAll(List.of(category1, category2, category3));
			restaurant1.getCategories().add(restaurantCategory1);
			restaurant2.getCategories().add(restaurantCategory2);
			restaurant3.getCategories().add(restaurantCategory3);
			restaurantLocationRepository.saveAll(List.of(location1, location2, location3));
			restaurant1.setLocation(location1);
			restaurant2.setLocation(location2);
			restaurant3.setLocation(location3);
			restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			List<Long> longList = restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList();
			assertArrayEquals(List.of(restaurant1.getId(), restaurant3.getId(), restaurant2.getId()).toArray(),
				longList.toArray());
		}

		@DisplayName("필터에 맞게 값을 가져오고 거리순으로 정렬 : 성공")
		@Test
		void success5() {

			// Given
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
			List<RestaurantMenu> menus1 = new ArrayList<>();
			List<RestaurantMenu> menus2 = new ArrayList<>();
			List<RestaurantMenu> menus3 = new ArrayList<>();

			FoodType foodType1 = FoodType.builder()
				.name("한식")
				.build();
			FoodType foodType2 = FoodType.builder()
				.name("중식")
				.build();
			FoodType foodType3 = FoodType.builder()
				.name("양식")
				.build();

			Restaurant restaurant1 = Restaurant.builder()
				.name("초밥회식집가게")
				.score(100)
				.menus(menus1)
				.foodType(foodType1)
				.categories(new ArrayList<>())
				.build();
			Restaurant restaurant2 = Restaurant.builder()
				.name("돼지국수가게")
				.score(50)
				.menus(menus2)
				.foodType(foodType2)
				.categories(new ArrayList<>())
				.build();
			Restaurant restaurant3 = Restaurant.builder()
				.name("돈까스가게")
				.score(30)
				.menus(menus3)
				.foodType(foodType3)
				.categories(new ArrayList<>())
				.build();

			RestaurantLocation location1 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5036)))
				.restaurant(restaurant1)
				.build();
			RestaurantLocation location2 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5035)))
				.restaurant(restaurant2)
				.build();
			RestaurantLocation location3 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5034)))
				.restaurant(restaurant3)
				.build();

			menus1.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant1).mainMenu(true).name("테스트").build());
			menus1.add(RestaurantMenu.builder().price(100000).restaurant(restaurant1).build());
			menus1.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant1)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 음식")
				.build());

			menus2.add(RestaurantMenu.builder().price(10000).restaurant(restaurant2).mainMenu(true).name("초밥").build());
			menus2.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant2).mainMenu(true).name("돼지국밥").build());

			menus3.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant3).mainMenu(true).name("짜장면").build());
			menus3.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant3).mainMenu(true).name("아메리카노").build());

			Category category1 = Category.builder()
				.name("카테고리1")
				.build();
			RestaurantCategory restaurantCategory1 = RestaurantCategory
				.builder()
				.restaurant(restaurant1)
				.category(category1)
				.build();
			Category category2 = Category.builder()
				.name("카테고리2")
				.build();
			RestaurantCategory restaurantCategory2 = RestaurantCategory
				.builder()
				.restaurant(restaurant2)
				.category(category2)
				.build();
			Category category3 = Category.builder()
				.name("초밥")
				.build();
			RestaurantCategory restaurantCategory3 = RestaurantCategory
				.builder()
				.restaurant(restaurant3)
				.category(category3)
				.build();

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("초밥")
				.maxDistance(10.0)
				.sort("거리순")
				// .foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();
			// when
			categoryRepository.saveAll(List.of(category1, category2, category3));
			restaurant1.getCategories().add(restaurantCategory1);
			restaurant2.getCategories().add(restaurantCategory2);
			restaurant3.getCategories().add(restaurantCategory3);
			restaurantLocationRepository.saveAll(List.of(location1, location2, location3));
			restaurant1.setLocation(location1);
			restaurant2.setLocation(location2);
			restaurant3.setLocation(location3);
			restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			List<Long> longList = restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList();
			assertArrayEquals(List.of(restaurant3.getId(), restaurant2.getId(), restaurant1.getId()).toArray(),
				longList.toArray());
		}

		@DisplayName("필터에 맞게 값을 가져오고 별점순으로 정렬 : 성공")
		@Test
		void success6() {

			// Given
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
			List<RestaurantMenu> menus1 = new ArrayList<>();
			List<RestaurantMenu> menus2 = new ArrayList<>();
			List<RestaurantMenu> menus3 = new ArrayList<>();

			FoodType foodType1 = FoodType.builder()
				.name("한식")
				.build();
			FoodType foodType2 = FoodType.builder()
				.name("중식")
				.build();
			FoodType foodType3 = FoodType.builder()
				.name("양식")
				.build();

			Restaurant restaurant1 = Restaurant.builder()
				.name("초밥회식집가게")
				.score(10)
				.menus(menus1)
				.foodType(foodType1)
				.categories(new ArrayList<>())
				.build();
			Restaurant restaurant2 = Restaurant.builder()
				.name("돼지국수가게")
				.score(50)
				.menus(menus2)
				.foodType(foodType2)
				.categories(new ArrayList<>())
				.build();
			Restaurant restaurant3 = Restaurant.builder()
				.name("돈까스가게")
				.score(30)
				.menus(menus3)
				.foodType(foodType3)
				.categories(new ArrayList<>())
				.build();

			RestaurantLocation location1 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5036)))
				.restaurant(restaurant1)
				.build();
			RestaurantLocation location2 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5035)))
				.restaurant(restaurant2)
				.build();
			RestaurantLocation location3 = RestaurantLocation.builder()
				.location(factory.createPoint(new Coordinate(12.0521, 37.5034)))
				.restaurant(restaurant3)
				.build();

			menus1.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant1).mainMenu(true).name("테스트").build());
			menus1.add(RestaurantMenu.builder().price(100000).restaurant(restaurant1).build());
			menus1.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant1)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 음식")
				.build());

			menus2.add(RestaurantMenu.builder().price(10000).restaurant(restaurant2).mainMenu(true).name("초밥").build());
			menus2.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant2).mainMenu(true).name("돼지국밥").build());

			menus3.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant3).mainMenu(true).name("짜장면").build());
			menus3.add(
				RestaurantMenu.builder().price(100000).restaurant(restaurant3).mainMenu(true).name("아메리카노").build());

			Category category1 = Category.builder()
				.name("카테고리1")
				.build();
			RestaurantCategory restaurantCategory1 = RestaurantCategory
				.builder()
				.restaurant(restaurant1)
				.category(category1)
				.build();
			Category category2 = Category.builder()
				.name("카테고리2")
				.build();
			RestaurantCategory restaurantCategory2 = RestaurantCategory
				.builder()
				.restaurant(restaurant2)
				.category(category2)
				.build();
			Category category3 = Category.builder()
				.name("초밥")
				.build();
			RestaurantCategory restaurantCategory3 = RestaurantCategory
				.builder()
				.restaurant(restaurant3)
				.category(category3)
				.build();

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("초밥")
				.maxDistance(10.0)
				.sort("별점순")
				// .foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();
			// when
			categoryRepository.saveAll(List.of(category1, category2, category3));
			restaurant1.getCategories().add(restaurantCategory1);
			restaurant2.getCategories().add(restaurantCategory2);
			restaurant3.getCategories().add(restaurantCategory3);
			restaurantLocationRepository.saveAll(List.of(location1, location2, location3));
			restaurant1.setLocation(location1);
			restaurant2.setLocation(location2);
			restaurant3.setLocation(location3);
			restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			List<Long> longList = restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList();
			assertArrayEquals(List.of(restaurant2.getId(), restaurant3.getId(), restaurant1.getId()).toArray(),
				longList.toArray());
		}

		@DisplayName("가격 내에 검색문자열에 맞는 값이 없음 : 실패")
		@Test
		void failCase1() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 국밥")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("테스트 김치찌개")
				.maxDistance(10.0)
				// .sort("정확도순")
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(1000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("음식타입이 일치하지 않음 : 실패")
		@Test
		void failCase2() {

			// Given
			GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
			List<RestaurantMenu> menus = new ArrayList<>();
			String typeName = "중식";

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("테스트")
				.maxDistance(10.0)
				// .sort("정확도순")
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 37.5033 + " " + 12.0521 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}

		@DisplayName("거리 내부에 음식점이 존재하지 않음 : 실패")
		@Test
		void failCase3() {

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
				.location(factory.createPoint(new Coordinate(12.0521, 37.5033)))
				.restaurant(restaurant)
				.build();

			menus.add(
				RestaurantMenu.builder().price(10000).restaurant(restaurant).mainMenu(true).name("테스트 음식").build());
			menus.add(RestaurantMenu.builder().price(100000).restaurant(restaurant).build());
			menus.add(RestaurantMenu.builder()
				.price(5000)
				.restaurant(restaurant)
				.orderNumber(1)
				.mainMenu(true)
				.name("테스트 콜라")
				.build());

			RestaurantSearchCondition searchCondition = RestaurantSearchCondition.builder()
				.searchString("테스트")
				.maxDistance(10.0)
				// .sort("정확도순")
				.foodTypeName("한식")
				.mySqlPoint("POINT(" + 12.0521 + " " + 37.5033 + ")")
				.pageable(PageRequest.of(0, 100))
				.maxPrice(100000)
				.build();

			// when
			restaurantLocationRepository.save(location);
			restaurant.setLocation(location);
			restaurantRepository.save(restaurant);
			entityManager.flush();
			entityManager.clear();

			List<RestaurantSummary> restaurantSummaries = restaurantRepository.searchRestaurantContainStringSearch(
				searchCondition).getContent();

			// then
			assertFalse(restaurantSummaries
				.stream()
				.map(RestaurantSummary::getRestaurantId)
				.toList()
				.contains(restaurant.getId()));
		}
	}
}