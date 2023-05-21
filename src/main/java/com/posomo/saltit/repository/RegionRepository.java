package com.posomo.saltit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.posomo.saltit.domain.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {

	@Query("select r from Region r left join fetch r.districtList")
	List<Region> findAllWithDistrictList();
}
