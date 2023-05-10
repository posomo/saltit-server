package com.posomo.saltit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;

public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation, Long> {
}
