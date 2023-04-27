package com.posomo.saltit.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantInformation;

public interface RestaurantInformationRepository extends JpaRepository<RestaurantInformation, Long> {
}
