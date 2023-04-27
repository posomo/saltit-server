package com.posomo.saltit.domain.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@Schema(description = "레스토랑 정보")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantSummary {
    @Schema(description = "가게 사진", defaultValue = "null")
    private String titleImageUrl;
    @Schema(description = "가게 이름", example = "육쌈냉면")
    private String restaurantName;
    @Schema(description = "맛집 점수", example = "85")
    private Integer rating;
    @Schema(description = "메인 메뉴 가격", example = "8500")
    private Integer mainMenuPrice;
    @Schema(description = "메인 메뉴 이름", example = "물냉면")
    private String mainMenuName;
    @Schema(description = "식당 카테고리 이름", example = "한식")
    private String categoryName;
    @Schema(description = "거리(단위 m)", example = "300")
    private Double distance;

    public static RestaurantSummary of(Object[] objects) {
        return new RestaurantSummary(
            objects[0] == null ? null : ((String)objects[0]),
            objects[1] == null ? null : ((String)objects[1]),
            objects[2] == null ? null : ((Integer)objects[2]),
            objects[3] == null ? null : ((Integer)objects[3]),
            objects[4] == null ? null : ((String)objects[4]),
            objects[5] == null ? null : ((String)objects[5]),
            objects[6] == null ? null : ((Double)objects[6])
        );
    }
}
