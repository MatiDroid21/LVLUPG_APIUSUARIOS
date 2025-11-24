package com.lvlupgamer.usuarios.apiusuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        // ⭐ Nombre del esquema de seguridad
        final String securitySchemeName = "ApiKeyAuth";
        
        return new OpenAPI()
            .info(new Info()
                .title("API Level Up Gamer")
                .version("v1")
                .description("API REST para gestión de usuarios, productos y pedidos"))
            // ⭐ Agregar el requisito de seguridad
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
            // ⭐ Definir el esquema de seguridad
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name("x-api-key")  // ⭐ Nombre del header
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .description("Ingresa tu API Key")));
    }
}
