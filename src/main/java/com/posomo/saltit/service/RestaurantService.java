package com.posomo.saltit.service;
import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
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

    public RestaurantDetailResponse getRestaurantDetail(long restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdWithMenus(restaurantId)
            .orElseThrow(() -> new NoRecordException(String.format("restaurantId = %d record not found", restaurantId)));
        return RestaurantDetailResponse.of(restaurant);
    }

    private Slice<RestaurantSummary> getRestaurantSummaryFromObjects(Slice<Object[]> restaurantSummaries){
        return restaurantSummaries.map(objects-> RestaurantSummary.create(
            objects[0] == null ? null : ((Long)objects[0]),
            objects[1] == null ? null : ((String)objects[1]),
            objects[2] == null ? null : ((String)objects[2]),
            objects[3] == null ? null : ((Integer)objects[3]),
            objects[4] == null ? null : ((Integer)objects[4]),
            objects[5] == null ? null : ((String)objects[5]),
            objects[6] == null ? null : ((String)objects[6]),
            objects[7] == null ? null : ((Double)objects[7])
        ));
    }
}
