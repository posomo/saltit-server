package com.posomo.saltit.domain.restaurant.entity.manyToOne;


import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorColumn(name = "LAST_ORDER")
public class RestaurantLastOrderTime extends RestaurantTime{
}
