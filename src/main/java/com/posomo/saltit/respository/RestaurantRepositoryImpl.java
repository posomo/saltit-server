package com.posomo.saltit.respository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.posomo.saltit.domain.restaurant.entity.QRestaurant;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Restaurant> findRestaurantBy() {
		QRestaurant restaurant = QRestaurant.restaurant;
		return jpaQueryFactory
			.selectFrom(restaurant)
			.fetch();
	}
}
