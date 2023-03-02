package com.posomo.saltit.domain.restaurant.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantSummary {
    private String titleImageUrl;
    private String restaurantName;
    private String rating;
    private BigDecimal mainMenuPrice;
    private String mainMenuName;
    private String categoryName;
    private Long distance;
}
