package com.trova_app.config;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return (factory) -> factory.addConnectorCustomizers((Connector connector) -> {
            // 10 MB = 10 * 1024 * 1024 bytes
            connector.setMaxPostSize(10 * 1024 * 1024);
        });
    }
}