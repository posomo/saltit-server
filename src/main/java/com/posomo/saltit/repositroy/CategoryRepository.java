package com.posomo.saltit.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
