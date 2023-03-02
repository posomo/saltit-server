package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTimeRepository extends JpaRepository<RestaurantTime,Long> {
}
