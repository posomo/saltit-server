package com.posomo.saltit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.posomo.saltit.domain.region.entity.District;

public interface DistrictRepository extends JpaRepository<District, Integer> {
}
