package com.agendador.api_agendador.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Medical Appointment Scheduling API",
                description = "This API allows the management of patients, doctors, schedules, and medical appointment bookings. Includes JWT authentication.",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Render ENV",
                        url = "https://sistema-consultas-medicas.onrender.com"),
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080")
        },
        security = {
                @SecurityRequirement(
                        name = "Bearer Authentication"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "JWT-based authentication. Use a valid token to access endpoints.",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
}