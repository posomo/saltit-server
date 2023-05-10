package com.posomo.saltit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantCategory;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
}
