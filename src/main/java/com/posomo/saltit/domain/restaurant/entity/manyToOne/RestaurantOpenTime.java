package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorColumn(name = "OPEN")
public class RestaurantOpenTime extends RestaurantTime{
}
