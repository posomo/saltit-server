package com.posomo.saltit.swagger;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
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

	@Value("${server.nginx-url:http://localhost:8080}")
	private String nginxUrl;

	@Bean
	public GroupedOpenApi chatOpenApi() {
		String[] paths = {"/**"};

		return GroupedOpenApi.builder()
			.group("saltit api v1")
			.pathsToMatch(paths)
			.build();
	}

	@Bean
	public OpenAPI customOpenAPI() {
		Server server = new Server();
		server.setUrl(nginxUrl);
		server.description("현재 Swagger가 요청을 보내는 위치");
		return new OpenAPI().servers(List.of(server));
	}
}
