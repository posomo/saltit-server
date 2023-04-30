package com.posomo.saltit.respository;

import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query(value = "select r.id, r.title_image_url,r.name,r.score,rm.price,rm.name,ft.name," +
            "ROUND(ST_Distance_Sphere(rl.location,ST_PointFromText(:location,4326))) as distance " +
            "from restaurant r join food_type ft on r.food_type_id = ft.id " +
            "join restaurant_menu rm on r.id = rm.restaurant_id join restaurant_location rl on r.id=rl.restaurant_id " +
            "where rm.order_number = 1 " +
            "and (:maxPrice is null or rm.price <= :maxPrice) " +
            "and (:foodTypeName is null or ft.name = :foodTypeName) " +
            "and ST_Within(location, getDistanceMBR(ST_PointFromText(:location, 4326),:maxDistance))" +
            "order by r.score DESC",
            nativeQuery = true)
    Slice<Object[]> findRestaurantByFilter(@Param(value = "maxPrice")Integer maxPrice,
                                           @Param(value = "foodTypeName")String foodTypeName,
                                           @Param(value = "location")String location,
                                           @Param(value = "maxDistance")Double maxDistance,
                                           Pageable pageable);

    @Query(value = "select r from Restaurant r left join fetch r.menus left join fetch r.information left join fetch r.location where r.id=:id")
    Optional<Restaurant> findByIdWithMenus(@Param(value = "id") long id);
}
