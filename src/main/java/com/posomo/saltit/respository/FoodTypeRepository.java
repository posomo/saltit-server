package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodTypeRepository extends JpaRepository<FoodType,Long> {
}
