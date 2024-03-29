package com.posomo.saltit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantTime;

public interface RestaurantTimeRepository extends JpaRepository<RestaurantTime, Long> {
}
