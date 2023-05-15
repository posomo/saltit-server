package com.posomo.saltit.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.QCategory;
import com.posomo.saltit.domain.restaurant.entity.QFoodType;
import com.posomo.saltit.domain.restaurant.entity.QRestaurant;
import com.posomo.saltit.domain.restaurant.entity.QRestaurantCategory;
import com.posomo.saltit.domain.restaurant.entity.QRestaurantLocation;
import com.posomo.saltit.domain.restaurant.entity.QRestaurantMenu;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	QRestaurant restaurant = QRestaurant.restaurant;
	QFoodType foodType = QFoodType.foodType;
	QRestaurantMenu restaurantMenu = QRestaurantMenu.restaurantMenu;
	QRestaurantLocation restaurantLocation = QRestaurantLocation.restaurantLocation;
	QRestaurantCategory restaurantCategory = QRestaurantCategory.restaurantCategory;
	QCategory category = QCategory.category;

	@Override
	public Slice<RestaurantSummary> searchRestaurant(RestaurantFilterRequest filterRequest) {
		Pageable pageable = filterRequest.createPageRequest();
		return getSummarySlice(pageable, getRestaurantSummariesNotContainSearch(filterRequest, pageable));
	}

	@Override
	public Slice<RestaurantSummary> searchRestaurantContainStringSearch(
		RestaurantFilterRequest filterRequest) {
		Pageable pageable = filterRequest.createPageRequest();
		return getSummarySlice(pageable, getRestaurantSummariesContainSearch(filterRequest, pageable));
	}

	private SliceImpl<RestaurantSummary> getSummarySlice(Pageable pageable,
		List<RestaurantSummary> content) {
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(content, pageable, hasNext);
	}

	private List<RestaurantSummary> getRestaurantSummariesNotContainSearch(RestaurantFilterRequest filterRequest,
		Pageable pageable) {

		JPAQuery<RestaurantSummary> restaurantSummaryJPAQuery = defaultJpaQuery(filterRequest, pageable)
			.groupBy(restaurant.id);

		addOrderStandard(filterRequest, restaurantSummaryJPAQuery);
		return restaurantSummaryJPAQuery
			.fetch();
	}

	private List<RestaurantSummary> getRestaurantSummariesContainSearch(RestaurantFilterRequest filterRequest,
		Pageable pageable) {

		JPAQuery<RestaurantSummary> restaurantSummaryJPAQuery = defaultJpaQuery(filterRequest, pageable)
			.leftJoin(restaurant.categories, restaurantCategory)
			.leftJoin(restaurantCategory.category, category)
			.where(category.name.like("%" + filterRequest.getOptions().getSearch() + "%")
				.or(restaurantMenu.name.like("%" + filterRequest.getOptions().getSearch() + "%"))
				.or(restaurant.name.like("%" + filterRequest.getOptions().getSearch() + "%")))
			.groupBy(restaurant.id);

		addOrderStandard(filterRequest, restaurantSummaryJPAQuery);
		return restaurantSummaryJPAQuery
			.fetch();
	}

	private JPAQuery<RestaurantSummary> defaultJpaQuery(RestaurantFilterRequest filterRequest, Pageable pageable) {
		return jpaQueryFactory
			.select(getRestaurantSummaryConstructorExpression(restaurant, foodType, restaurantMenu,
				getDoubleDistanceExpression(filterRequest.computeMySqlPoint())))
			.from(restaurant)
			.innerJoin(restaurant.menus, restaurantMenu)
			.innerJoin(restaurant.location, restaurantLocation)
			.innerJoin(restaurant.foodType, foodType)
			.where(restaurantMenu.price.loe(filterRequest.getMaxPrice()))
			.where(foodType.name.eq(filterRequest.getFoodTypeName()))
			.where(
				withInByDistance(restaurantLocation, filterRequest.computeMySqlPoint(), filterRequest.getMaxDistance()))
			.where(restaurantMenu.mainMenu.eq(true))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1);
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
			restaurant.id.count(),
			getLongitudeExpression(),
			getLatitudeExpression(),
			numberExpression);
	}

	private BooleanTemplate withInByDistance(QRestaurantLocation restaurantLocation, String userPoint,
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


	private void addOrderStandard(RestaurantFilterRequest filterRequest,
		JPAQuery<RestaurantSummary> restaurantSummaryJPAQuery) {
		if (filterRequest.getOptions().getSort() == null || !filterRequest.getOptions().getSort().equals("거리순")) {
			// default 별점순
			restaurantSummaryJPAQuery.orderBy(restaurant.score.desc());
		} else {
			restaurantSummaryJPAQuery.orderBy(getDoubleDistanceExpression(filterRequest.computeMySqlPoint()).asc());
		}
	}
}
