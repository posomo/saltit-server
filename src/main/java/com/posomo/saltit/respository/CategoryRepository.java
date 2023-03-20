package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
