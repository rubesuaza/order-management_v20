package com.example.management.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para la aplicación.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            // TODO(user): Reemplazar con los orígenes específicos de tu frontend en producción
            .allowedOrigins("http://localhost:3000", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("Content-Type", "Authorization", "X-Requested-With");
    }
}
