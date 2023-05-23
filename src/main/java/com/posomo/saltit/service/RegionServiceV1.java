package com.posomo.saltit.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.posomo.saltit.domain.region.dto.RegionListResponseDto;
import com.posomo.saltit.domain.region.entity.Region;
import com.posomo.saltit.repository.DistrictRepository;
import com.posomo.saltit.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegionServiceV1 implements RegionService {
	private final RegionRepository regionRepository;

	@Override
	public RegionListResponseDto getRegionList() {
		List<Region> regionList = regionRepository.findAllWithDistrictList();
		return RegionListResponseDto.of(regionList);
	}
}
