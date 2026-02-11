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
            // Configuración de CORS: en producción, reemplazar con los orígenes específicos del frontend
            // usando variables de entorno o configuración externa
            .allowedOrigins(getAllowedOrigins())
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("Content-Type", "Authorization", "X-Requested-With");
    }
    
    private String[] getAllowedOrigins() {
        String originsEnv = System.getenv("CORS_ALLOWED_ORIGINS");
        if (originsEnv != null && !originsEnv.trim().isEmpty()) {
            return originsEnv.split(",");
        }
        // Valores por defecto para desarrollo
        return new String[]{"http://localhost:3000", "http://localhost:8080"};
    }
}
