package com.posomo.saltit.domain.region.dto;

import java.util.ArrayList;
import java.util.List;

import com.posomo.saltit.domain.region.entity.District;
import com.posomo.saltit.domain.region.entity.Region;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegionListResponseDto {
	List<String> area = new ArrayList<>();
	List<List<String>> region = new ArrayList<>();

	public static RegionListResponseDto of(List<Region> regionList) {
		List<String> area = regionList.stream().map(Region::getName).toList();
		List<List<String>> region = regionList.stream().map(regionEntity -> regionEntity.getDistrictList().stream().map(District::getName).toList()).toList();
		return new RegionListResponseDto(area, region);
	}
}
