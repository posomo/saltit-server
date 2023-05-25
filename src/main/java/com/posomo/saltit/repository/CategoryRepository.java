package com.posomo.saltit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.restaurant.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
