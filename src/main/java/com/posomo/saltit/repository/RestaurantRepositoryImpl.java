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
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
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

		return jpaQueryFactory.select(Projections.constructor(RestaurantSummary.class,
				restaurant.id,
				restaurant.titleImageUrl,
				restaurant.name,
				restaurant.score,
				restaurantMenu.price,
				restaurantMenu.name,
				foodType.name,
				getCountExpression(),
				getLongitudeExpression(),
				getLatitudeExpression(),
				getDistanceExpression(searchCondition.getMySqlPoint())
			))
			.from(restaurant)
			.innerJoin(restaurant.menus, restaurantMenu)
			.innerJoin(restaurant.location, restaurantLocation)
			.innerJoin(restaurant.foodType, foodType)
			.where(restaurantMenu.price.loe(searchCondition.getMaxPrice()),
				foodTypeNameEq(searchCondition.getFoodTypeName()),
				restaurantMenu.mainMenu.eq(true),
				withInByDistance(searchCondition.getMySqlPoint(), searchCondition.getMaxDistance())
			)
			.groupBy(restaurant.id)
			.orderBy(addOrderStandard(searchCondition.getSort(), searchCondition.getMySqlPoint(), searchCondition.getSearchString()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
	}


	private List<RestaurantSummary> getRestaurantSummariesContainSearch(RestaurantSearchCondition searchCondition, Pageable pageable) {

		// Expression<Integer> priceDifferenceExpression = getPriceDifference();

		return jpaQueryFactory.select(Projections.constructor(RestaurantSummary.class,
				restaurant.id,
				restaurant.titleImageUrl,
				restaurant.name,
				restaurant.score,
				restaurantMenu.price,
				restaurantMenu.name,
				foodType.name,
				getCountExpression(),
				getLongitudeExpression(),
				getLatitudeExpression(),
				getDistanceExpression(searchCondition.getMySqlPoint())
			))
			.from(restaurant)
			.innerJoin(restaurant.menus, restaurantMenu)
			.innerJoin(restaurant.location, restaurantLocation)
			.innerJoin(restaurant.foodType, foodType)
			.where(restaurantMenu.price.loe(searchCondition.getMaxPrice()),
				foodTypeNameEq(searchCondition.getFoodTypeName()),
				restaurantMenu.mainMenu.eq(true),
				withInByDistance(searchCondition.getMySqlPoint(), searchCondition.getMaxDistance()),
				restaurant.id.in(
					JPAExpressions.selectDistinct(restaurantCategory.restaurant.id)
						.from(restaurantCategory)
						.where(restaurantCategory.category.id.in(
							JPAExpressions.select(category.id)
								.from(category)
								.where(category.name.like("%" + searchCondition.getSearchString() + "%"))
						))
				).or(restaurant.id.in(
					JPAExpressions.selectDistinct(restaurantMenu.restaurant.id)
						.from(restaurantMenu)
						.where(restaurantMenu.name.like("%" + searchCondition.getSearchString() + "%"), restaurantMenu.price.loe(searchCondition.getMaxPrice()))
				)).or(restaurant.name.like("%" + searchCondition.getSearchString() + "%"))
			)
			.groupBy(restaurant.id)
			.orderBy(addOrderStandard(searchCondition.getSort(), searchCondition.getMySqlPoint(), searchCondition.getSearchString()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
	}

	private NumberExpression<Integer> getPriceDifference() {
		return Expressions.numberTemplate(Integer.class,
			"min(10000 - {0}) over (partition by {1})", restaurantMenu.price, restaurant.id).as("price_difference");
	}

	private Expression<Integer> getCountExpression() {
		return Expressions.numberTemplate(Integer.class,
			"count({0})", restaurant.id).as("count");
		// return Expressions.numberTemplate(Integer.class,
		// 	"count({0}) over (partition by {1})", restaurant.id, restaurant.id).as("count");
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

	private OrderSpecifier addOrderStandard(String orderBy, String pointString, String searchString) {

		if (orderBy == null) {
			return OrderByNull.DEFAULT;
		} else if (orderBy.equals("거리순")) {
			return getDistanceExpression(pointString).asc();
		} else if (orderBy.equals("정확도순") && searchString != null) {
			return getAccurateExpression(searchString);
		} else if (orderBy.equals("별점순")) {
			return restaurant.score.desc();
		}
		return OrderByNull.DEFAULT;

	}

	private NumberExpression<Double> getDistanceExpression(String point) {
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

	private OrderSpecifier<Integer> getAccurateExpression(String searchString) {
		return new CaseBuilder()
			.when(restaurant.name.like("%" + searchString + "%")).then(1)
			.when(restaurant.id.in(
				JPAExpressions.selectDistinct(restaurantCategory.restaurant.id)
					.from(restaurantCategory)
					.where(restaurantCategory.category.id.in(
						JPAExpressions.select(category.id)
							.from(category)
							.where(category.name.like("%" + searchString + "%"))
					))
			)).then(2)
			.when(restaurant.id.in(
				JPAExpressions.selectDistinct(restaurantMenu.restaurant.id)
					.from(restaurantMenu)
					.where(restaurantMenu.name.like("%" + searchString + "%"))
			)).then(3)
			.otherwise(3)
			.asc();
	}
}
