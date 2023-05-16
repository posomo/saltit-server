package com.posomo.saltit.domain.restaurant.dto;

import org.springframework.data.domain.Pageable;

import lombok.Data;

@Data(staticConstructor = "of")
public class RestaurantSearchCondition {

	private Double maxDistance;

	private Integer maxPrice;

	private Pageable pageable;

	private String mySqlPoint;

	private String searchString;

	private String sort;

	private String foodTypeName;

	public RestaurantSearchCondition(RestaurantFilterRequest filterRequest) {
		this.maxDistance = filterRequest.getMaxDistance();
		this.maxPrice = filterRequest.getMaxPrice();
		this.pageable = filterRequest.createPageRequest();
		this.mySqlPoint = filterRequest.computeMySqlPoint();
		this.searchString = filterRequest.getOptions().getSearch();
		this.sort = filterRequest.getOptions().getSort();
		this.foodTypeName = filterRequest.getOptions().getFoodTypeName();
	}

}
