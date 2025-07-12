package com.trovaApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TROVA Backend API")
                        .version("1.0")
                        .description("API documentation for Trova record label management system")
                        .contact(new Contact().name("Pablo Moscón").email("mosconpablo@gmail.com"))
                        .license(new License().name("MIT License")))
                .addTagsItem(new Tag().name("albums").description("Operations related to Albums"))
                .addTagsItem(new Tag().name("artists").description("Operations related to Artists"))
                .addTagsItem(new Tag().name("auth").description("Operations related to Authentications"))
                .addTagsItem(new Tag().name("songs").description("Operations related to Songs"))
                .addTagsItem(new Tag().name("stats").description("Operations related to Stats"))
                .addTagsItem(new Tag().name("users").description("Operations related to Users"));
    }
}

