package com.posomo.saltit.service;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import com.posomo.saltit.respository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    public Slice<RestaurantSummary> getRestaurantSummaries(RestaurantFilterRequest filterRequest){
        String location = "POINT("+filterRequest.getUserLatitude()+" "+filterRequest.getUserLongitude()+")";
        PageRequest pageRequest = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        Slice<Object[]> resultObjects = restaurantRepository.findRestaurantByFilter(filterRequest.getMaxPrice(),
                filterRequest.getFoodTypeName(),location, filterRequest.getMaxDistance()
        ,pageRequest);
        return getRestaurantSummaryFromObjects(resultObjects);
    }
    private Slice<RestaurantSummary> getRestaurantSummaryFromObjects(Slice<Object[]> restaurantSummaries){
        return restaurantSummaries.map(objects-> RestaurantSummary.create(
            objects[0] == null ? null : ((String)objects[0]),
            objects[1] == null ? null : ((String)objects[1]),
            objects[2] == null ? null : ((Integer)objects[2]),
            objects[3] == null ? null : ((Integer)objects[3]),
            objects[4] == null ? null : ((String)objects[4]),
            objects[5] == null ? null : ((String)objects[5]),
            objects[6] == null ? null : ((Double)objects[6])
        ));
    }
}
