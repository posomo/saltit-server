package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import java.util.UUID;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    @Query(value = "select r from Restaurant r join r.categories rc join r.menus rm " +
            "ST_Distance_sphere(Poin,Point)",nativeQuery = true)
    Slice<Restaurant> findRestaurantByFilter();
}
