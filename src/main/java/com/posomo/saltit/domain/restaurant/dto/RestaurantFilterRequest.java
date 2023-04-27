package com.posomo.saltit.domain.restaurant.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "홈 화면 레스토랑 필터 요청 정보 (/api/v1/home/restaurant-summary)")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantFilterRequest {
	@Schema(description = "식당 카테고리 이름", example = "한식", defaultValue = "null")
	private String foodTypeName;

	@Schema(description = "최대 검색 거리(m)", example = "1000", defaultValue = "10000")
	@PositiveOrZero
	@NotNull
	private Double maxDistance;

	@Schema(description = "가격 상한선", example = "8500", defaultValue = "40000")
	@Positive
	@NotNull
	private Integer maxPrice;

	@Schema(description = "요청 페이지 번호 (0부터 시작)", example = "1")
	@PositiveOrZero
	@NotNull
	private Integer page;

	@Schema(description = "요청 페이지 크기", example = "3")
	@NotNull
	@Positive
	private Integer size;

	@Schema(description = "사용자 경도(최소 소수점 8자리까지) -180~180", example = "127.0521000000")
	@NotNull
	private Double userLongitude;

	@Schema(description = "사용자 위도(최소 소수점 8자리까지) -90~90", example = "37.5033000000")
	@NotNull
	private Double userLatitude;

	public String computeMySqlPoint() {
		return "POINT(" + userLatitude + " " + userLongitude + ")";
	}

	public Pageable createPageRequest() {
		return PageRequest.of(page, size);
	}
}
