package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu,Long> {
}
