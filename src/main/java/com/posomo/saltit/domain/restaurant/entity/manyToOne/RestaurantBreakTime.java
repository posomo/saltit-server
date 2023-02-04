package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorColumn(name = "BREAK")
public class RestaurantBreakTime extends RestaurantTime{
}
