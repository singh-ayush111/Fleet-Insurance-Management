package com.htc.fleetmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	// Configures the OpenAPI documentation with JWT authentication support
	@Bean
	public OpenAPI customOpenAPI() {

		final String securitySchemeName = "bearerAuth";

		return new OpenAPI()
				.info(new Info().title("Fleet Insurance Policy Management API").version("1.0").description("JWT Authentication API"))
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new io.swagger.v3.oas.models.Components()
						.addSecuritySchemes(securitySchemeName,
								new SecurityScheme()
								.name(securitySchemeName)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")));
	}
}