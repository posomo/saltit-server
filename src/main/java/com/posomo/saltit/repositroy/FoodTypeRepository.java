package com.posomo.saltit.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.FoodType;

public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {
}
