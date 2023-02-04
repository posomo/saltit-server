package com.posomo.saltit.domain.restaurant.entity.oneToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Entity
@Getter
@Immutable
public class RestaurantMainMenuPriceRange extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private BigDecimal average;

}
