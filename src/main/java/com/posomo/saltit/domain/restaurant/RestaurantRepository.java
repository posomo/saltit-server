package com.posomo.saltit.domain.restaurant;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
}
