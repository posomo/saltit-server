package com.posomo.saltit.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;

// SwaggerConfig.java
@OpenAPIDefinition(
	info = @Info(
		title = "saltit API 명세서",
		description = "saltit API 명세서",
		version = "v1"
	)
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi chatOpenApi() {
		String[] paths = {"/**"};

		return GroupedOpenApi.builder()
			.group("saltit api v1")
			.pathsToMatch(paths)
			.build();
	}
}
