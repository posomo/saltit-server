package com.posomo.saltit.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(
			List.of("http://3.35.147.39:80", "http://3.35.147.39"));
		configuration.addAllowedHeader("*");
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);

		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}
}