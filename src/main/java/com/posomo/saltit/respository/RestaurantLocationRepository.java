package com.posomo.saltit.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;

public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation, Long> {
}
