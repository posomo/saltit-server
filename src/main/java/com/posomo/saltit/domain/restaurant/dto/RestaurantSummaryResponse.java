package com.posomo.saltit.domain.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

import org.springframework.data.domain.Slice;

@Getter
@Schema(description = "레스토랑 정보 리스보")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RestaurantSummaryResponse {
    @Schema(description = "레스토랑 정보", defaultValue = "null")
    public List<RestaurantSummary> restaurantSummaries;
    @Schema(description = "다음 페이지 존재 여부", example = "false",defaultValue = "null")
    public boolean hasNext;
    @Schema(description = "현재 페이지", example = "0", defaultValue = "null")
    public Integer page;
    @Schema(description = "한 페이지 크기", example = "3", defaultValue = "null")
    public Integer size;

    public static RestaurantSummaryResponse of(Slice<Object[]> restaurantSummaryObjectSlice) {
        Slice<RestaurantSummary> restaurantSummarySlice = restaurantSummaryObjectSlice.map(RestaurantSummary::of);

        List<RestaurantSummary> restaurantSummaries = restaurantSummarySlice.stream().toList();
        boolean hasNext = restaurantSummarySlice.hasNext();
        Integer pageNumber = restaurantSummarySlice.getPageable().getPageNumber();
        Integer pageSize = restaurantSummarySlice.getPageable().getPageSize();

        return new RestaurantSummaryResponse(restaurantSummaries, hasNext, pageNumber, pageSize);
    }
}
