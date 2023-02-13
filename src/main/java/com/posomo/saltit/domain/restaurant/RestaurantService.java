package com.posomo.saltit.domain.restaurant;

import com.posomo.saltit.domain.restaurant.dto.RestaurantCreateManyDto;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDto;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public List<UUID> createMany(RestaurantCreateManyDto restaurantCreateManyDto) {
        List<Restaurant> restaurants = restaurantCreateManyDto.getRestaurants().stream().map(RestaurantDto::toEntity).toList();
        restaurantRepository.saveAll(restaurants);
        return restaurants.stream().map(BaseEntity::getId).toList();
    }
}
