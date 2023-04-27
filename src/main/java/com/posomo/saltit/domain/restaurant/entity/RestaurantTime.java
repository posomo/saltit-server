package com.posomo.saltit.domain.restaurant.entity;

import java.time.LocalTime;

import com.posomo.saltit.domain.restaurant.enums.Day;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RestaurantTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "restaurant_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Restaurant restaurant;

	private LocalTime timeFrom;

	private LocalTime timeTo;

	@Enumerated(value = EnumType.STRING)
	private Day day;
}
