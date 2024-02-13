package br.com.CIUBank.loan.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI(@Value("${api.version}") String appVersion) {
	    return new OpenAPI()
	            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
	            .components(new io.swagger.v3.oas.models.Components()
	                    .addSecuritySchemes("bearerAuth",
	                            new SecurityScheme()
	                                    .name("bearerAuth")
	                                    .type(SecurityScheme.Type.HTTP)
	                                    .scheme("bearer")
	                                    .bearerFormat("JWT")
	                                    .description("JWT authentication required. Please insert the JWT token as follows: 'Bearer {your_token}'."))
	                    )
	            .info(new Info()
	                    .title("CIUBank Loan Service API")
	                    .version(appVersion)
	                    .description("This is the API documentation for the CIUBank Loan Service. It covers all endpoints used to manage loans, customer information, and payment processing. \n\n ## Authentication \n To obtain a token, use the authentication endpoint...")
	                    .termsOfService("http://ciubank.terms/tos")
	                    .contact(new Contact()
	                            .name("CIUBank API Support Team")
	                            .url("http://ciubank.support")
	                            .email("api-support@ciubank.com"))
	                    .license(new License()
	                            .name("Apache 2.0")
	                            .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
	            .addTagsItem(new Tag().name("Loan Management").description("API endpoints related to loan management."))
	            .addTagsItem(new Tag().name("User Authentication").description("Endpoints related to user authentication and authorization."))
	            .servers(List.of(
	                new Server().url("http://localhost:8080/api").description("Development Environment"),
	                new Server().url("https://api.ciubank.prod").description("Production Environment")
	            ));
	}

}
