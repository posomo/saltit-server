package com.posomo.saltit.respository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
class RestaurantRepositoryImplTest {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	EntityManager entityManager;


	@Transactional
	@DisplayName("getTodos_QueryDSL을 사용하여 To-Do 목록 조회")
	@Test
	void testGetTodos() {

		// Given

	}
}