package com.posomo.saltit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
}
