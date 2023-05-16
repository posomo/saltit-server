package com.posomo.saltit.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.posomo.saltit.domain.restaurant.dto.RestaurantSearchCondition;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.repository.support.OrderByNull;
import com.posomo.saltit.repository.support.SliceAdapter;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.posomo.saltit.domain.restaurant.entity.QRestaurant.restaurant;
import static com.posomo.saltit.domain.restaurant.entity.QFoodType.foodType;
import static com.posomo.saltit.domain.restaurant.entity.QRestaurantMenu.restaurantMenu;
import static com.posomo.saltit.domain.restaurant.entity.QRestaurantLocation.restaurantLocation;
import static com.posomo.saltit.domain.restaurant.entity.QRestaurantCategory.restaurantCategory;
import static com.posomo.saltit.domain.restaurant.entity.QCategory.category;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<RestaurantSummary> searchRestaurant(RestaurantSearchCondition searchCondition) {
		Pageable pageable = searchCondition.getPageable();
		return SliceAdapter.makeListToSlice(pageable,
			getRestaurantSummariesNotContainSearch(searchCondition, pageable));
	}

	@Override
	public Slice<RestaurantSummary> searchRestaurantContainStringSearch(
		RestaurantSearchCondition searchCondition) {
		Pageable pageable = searchCondition.getPageable();
		return SliceAdapter.makeListToSlice(pageable, getRestaurantSummariesContainSearch(searchCondition, pageable));
	}

	private List<RestaurantSummary> getRestaurantSummariesNotContainSearch(RestaurantSearchCondition searchCondition,
		Pageable pageable) {

		JPAQuery<RestaurantSummary> restaurantSummaryJPAQuery = defaultJpaQuery(searchCondition, pageable)
			.groupBy(restaurant.id);

		restaurantSummaryJPAQuery.orderBy(
			addOrderStandard(searchCondition.getSort(), searchCondition.getMySqlPoint()));
		return restaurantSummaryJPAQuery
			.fetch();
	}

	private List<RestaurantSummary> getRestaurantSummariesContainSearch(RestaurantSearchCondition searchCondition,
		Pageable pageable) {

		JPAQuery<RestaurantSummary> restaurantSummaryJPAQuery = defaultJpaQuery(searchCondition, pageable)
			.leftJoin(restaurant.categories, restaurantCategory)
			.leftJoin(restaurantCategory.category, category)
			.where(category.name.like("%" + searchCondition.getSearchString() + "%")
				.or(restaurantMenu.name.like("%" + searchCondition.getSearchString() + "%"))
				.or(restaurant.name.like("%" + searchCondition.getSearchString() + "%")))
			.groupBy(restaurant.id);

		restaurantSummaryJPAQuery.orderBy(
			addOrderStandard(searchCondition.getSort(), searchCondition.getMySqlPoint()));
		return restaurantSummaryJPAQuery
			.fetch();
	}

	private JPAQuery<RestaurantSummary> defaultJpaQuery(RestaurantSearchCondition searchCondition, Pageable pageable) {
		return jpaQueryFactory
			.select(
				getRestaurantSummaryConstructorExpression(getDoubleDistanceExpression(searchCondition.getMySqlPoint())))
			.from(restaurant)
			.innerJoin(restaurant.menus, restaurantMenu)
			.innerJoin(restaurant.location, restaurantLocation)
			.innerJoin(restaurant.foodType, foodType)
			.where(restaurantMenu.price.loe(searchCondition.getMaxPrice()))
			.where(foodTypeNameEq(searchCondition.getFoodTypeName()))
			.where(
				withInByDistance(searchCondition.getMySqlPoint(), searchCondition.getMaxDistance()))
			.where(restaurantMenu.mainMenu.eq(true))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1);
	}

	private ConstructorExpression<RestaurantSummary> getRestaurantSummaryConstructorExpression(
		NumberExpression<Double> numberExpression) {
		return Projections.constructor(RestaurantSummary.class,
			restaurant.id,
			restaurant.titleImageUrl,
			restaurant.name,
			restaurant.score,
			restaurantMenu.price,
			restaurantMenu.name,
			foodType.name,
			restaurant.id.count(),
			getLongitudeExpression(),
			getLatitudeExpression(),
			numberExpression);
	}

	private BooleanExpression foodTypeNameEq(String foodTypeName) {
		return foodTypeName != null ? foodType.name.eq(foodTypeName) : null;
	}

	private BooleanTemplate withInByDistance(String userPoint,
		double distance) {
		return Expressions.booleanTemplate(
			"ST_Within({0}, getDistanceMBR(ST_PointFromText({1}, 4326), {2}))",
			restaurantLocation.location, userPoint, distance);
	}

	private NumberExpression<Double> getDoubleDistanceExpression(String point) {
		return Expressions
			.numberTemplate(Double.class,
				"ST_Distance_Sphere({0}, ST_PointFromText({1}, 4326))",
				restaurantLocation.location, point);
	}

	private NumberExpression<Double> getLatitudeExpression() {
		return Expressions
			.numberTemplate(Double.class,
				"ST_X({0}) AS latitude",
				restaurantLocation.location);
	}

	private NumberExpression<Double> getLongitudeExpression() {
		return Expressions
			.numberTemplate(Double.class,
				"ST_Y({0}) AS longitude",
				restaurantLocation.location);
	}

	private OrderSpecifier addOrderStandard(String orderBy, String pointString) {

		if (orderBy == null) {
			return OrderByNull.DEFAULT;
		} else if (orderBy.equals("거리순")) {
			return getDoubleDistanceExpression(pointString).asc();
		} else {
			return restaurant.score.desc();
		}
	}
}
