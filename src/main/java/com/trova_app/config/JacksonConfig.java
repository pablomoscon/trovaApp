package com.trova_app.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // Enable case-insensitive enum handling (e.g., "rock_argentino" or "ROCK_ARGENTINO")
            builder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

            // Customize the ObjectMapper after it's built
            builder.postConfigurer(objectMapper -> {
                // Fail when unknown properties are received
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

                // Do NOT enable global default typing here for security reasons

                // If you ever need polymorphic deserialization, prefer annotations like:
                // @JsonTypeInfo + @JsonSubTypes on the specific base class/interface
            });
        };
    }
}
