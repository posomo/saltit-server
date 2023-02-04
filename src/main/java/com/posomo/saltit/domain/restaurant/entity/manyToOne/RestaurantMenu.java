package com.posomo.saltit.domain.restaurant.entity.manyToOne;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class RestaurantMenu extends BaseEntity {
    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;

    @Column(length = 20)
    private String name;
    private Integer price;
    @Column(name = "is_main_menu")
    private Boolean isMainMenu;
    @OneToOne(mappedBy = "restaurantMenu")
    private RestaurantMainMenu restaurantMainMenu;
    @Column(length = 300)
    private String pictureUrl;
}
