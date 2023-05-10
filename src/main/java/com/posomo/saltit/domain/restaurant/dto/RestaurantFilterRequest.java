package com.posomo.saltit.domain.restaurant.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "홈 화면 레스토랑 필터 요청 정보 (/api/v1/home/restaurant-summary)")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RestaurantFilterRequest {
	@Schema(description = "식당 카테고리 이름", example = "한식", defaultValue = "null")
	private String foodTypeName;

	@Schema(description = "최대 검색 거리(m)", example = "1000", defaultValue = "10000")
	@PositiveOrZero
	@NotNull
	private Double maxDistance;

	@Schema(description = "가격 상한선", example = "9500", defaultValue = "40000")
	@Positive
	@NotNull
	private Integer maxPrice;

	@Schema(description = "요청 페이지 번호 (0부터 시작)", example = "0")
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

	@Schema(description = "검색 문자", example = "낙지")
	@Nullable
	@Length(min = 1, max = 20)
	private String search;

	public int getMaxPrice() {
		return (maxPrice == null) ? 40000 : maxPrice;
	}

	public double getMaxDistance() {
		return ((maxDistance == null) ? 1000D : maxDistance)/1000;
	}
	public String computeMySqlPoint() {
		return "POINT(" + userLatitude + " " + userLongitude + ")";
	}

	public Pageable createPageRequest() {
		return PageRequest.of(page, size);
	}

	public RestaurantFilterRequest(String foodTypeName, Double maxDistance, Integer maxPrice, Integer page,
		Integer size,
		Double userLongitude, Double userLatitude) {
		this.foodTypeName = foodTypeName;
		this.maxDistance = maxDistance;
		this.maxPrice = maxPrice;
		this.page = page;
		this.size = size;
		this.userLongitude = userLongitude;
		this.userLatitude = userLatitude;
	}
}
