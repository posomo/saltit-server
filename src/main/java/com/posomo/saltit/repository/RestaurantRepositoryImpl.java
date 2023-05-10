package com.posomo.saltit.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.Category;
import com.posomo.saltit.domain.restaurant.entity.QCategory;
import com.posomo.saltit.domain.restaurant.entity.QFoodType;
import com.posomo.saltit.domain.restaurant.entity.QRestaurant;
import com.posomo.saltit.domain.restaurant.entity.QRestaurantCategory;
import com.posomo.saltit.domain.restaurant.entity.QRestaurantLocation;
import com.posomo.saltit.domain.restaurant.entity.QRestaurantMenu;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<RestaurantSummary> findRestaurantBy(RestaurantFilterRequest filterRequest) {
		if (filterRequest.getSearch() == null) {
			return getRestaurantSummariesNotContainSearch(filterRequest);
		}
		return getRestaurantSummariesContainSearch(filterRequest);
	}

	private List<RestaurantSummary> getRestaurantSummariesNotContainSearch(RestaurantFilterRequest filterRequest) {

		QRestaurant restaurant = QRestaurant.restaurant;
		QFoodType foodType = QFoodType.foodType;
		QRestaurantMenu restaurantMenu = QRestaurantMenu.restaurantMenu;
		QRestaurantLocation restaurantLocation = QRestaurantLocation.restaurantLocation;

		return jpaQueryFactory
			.select(getRestaurantSummaryConstructorExpression(restaurant, foodType, restaurantMenu,
				getDoubleDistanceExpression(restaurantLocation, filterRequest.computeMySqlPoint())))
			.from(restaurant)
			.innerJoin(restaurant.foodType, foodType)
			.innerJoin(restaurant.menus, restaurantMenu)
			.innerJoin(restaurant.location, restaurantLocation)
			.where(restaurantMenu.orderNumber.eq(1), restaurantMenu.price.loe(filterRequest.getMaxPrice()))
			.where(foodType.name.eq(filterRequest.getFoodTypeName()))
			.where(
				withInByDistance(restaurantLocation, filterRequest.computeMySqlPoint(), filterRequest.getMaxDistance()))
			.orderBy(restaurant.score.desc())
			.offset(filterRequest.getPage())
			.limit(filterRequest.getSize())
			.fetch();
	}

	private List<RestaurantSummary> getRestaurantSummariesContainSearch(RestaurantFilterRequest filterRequest) {

		QRestaurant restaurant = QRestaurant.restaurant;
		QFoodType foodType = QFoodType.foodType;
		QRestaurantMenu restaurantMenu = QRestaurantMenu.restaurantMenu;
		QRestaurantLocation restaurantLocation = QRestaurantLocation.restaurantLocation;
		QRestaurantCategory restaurantCategory = QRestaurantCategory.restaurantCategory;
		QCategory category = QCategory.category;

		return jpaQueryFactory
			.select(getRestaurantSummaryConstructorExpression(restaurant, foodType, restaurantMenu,
				getDoubleDistanceExpression(restaurantLocation, filterRequest.computeMySqlPoint())))
			.from(restaurant)
			.innerJoin(restaurant.foodType, foodType)
			.innerJoin(restaurant.menus, restaurantMenu)
			.innerJoin(restaurant.location, restaurantLocation)
			.leftJoin(restaurant.categories, restaurantCategory)
			.leftJoin(restaurantCategory.category, category)
			.where(restaurantMenu.price.loe(filterRequest.getMaxPrice()))
			.where(foodType.name.eq(filterRequest.getFoodTypeName()))
			.where(
				withInByDistance(restaurantLocation, filterRequest.computeMySqlPoint(), filterRequest.getMaxDistance()))
			.where(category.name.like("%" + filterRequest.getSearch() + "%")
				.or(restaurantMenu.name.like("%" + filterRequest.getSearch() + "%"))
				.or(restaurant.name.like("%" + filterRequest.getSearch() + "%")))
			.orderBy(restaurant.score.desc())
			.groupBy(restaurant.id)
			.offset(filterRequest.getPage())
			.limit(filterRequest.getSize())
			.fetch();
	}

	private BooleanTemplate withInByDistance(QRestaurantLocation restaurantLocation, String userPoint,
		double distance) {
		return Expressions.booleanTemplate(
			"ST_Within({0}, getDistanceMBR(ST_PointFromText({1}, 4326), {2}))",
			restaurantLocation.location, userPoint, distance);
	}

	private ConstructorExpression<RestaurantSummary> getRestaurantSummaryConstructorExpression(QRestaurant restaurant,
		QFoodType foodType,
		QRestaurantMenu restaurantMenu, NumberExpression<Double> numberExpression) {
		return Projections.constructor(RestaurantSummary.class,
			restaurant.id,
			restaurant.titleImageUrl,
			restaurant.name,
			restaurant.score,
			restaurantMenu.price,
			restaurantMenu.name,
			foodType.name,
			numberExpression);
	}

	private NumberExpression<Double> getDoubleDistanceExpression(QRestaurantLocation restaurantLocation, String point) {
		return Expressions
			.numberTemplate(Double.class,
				"ST_Distance_Sphere({0}, ST_PointFromText({1}, 4326))",
				restaurantLocation.location, point)
			.as(Expressions.numberPath(Double.class, "distance"));
	}
}
