package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation,Long> {
}
