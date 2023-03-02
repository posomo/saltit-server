package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.RestaurantInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantInformationRepository extends JpaRepository<RestaurantInformation,Long> {
}
