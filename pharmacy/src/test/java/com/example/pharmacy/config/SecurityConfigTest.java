/*
package com.example.pharmacy.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        SecurityConfig config = new SecurityConfig(null);
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void corsConfigurationSource_shouldHaveExpectedAllowedOriginsAndHeaders() {
        SecurityConfig config = new SecurityConfig(null);
        CorsConfigurationSource corsSource = config.corsConfigurationSource();

        assertNotNull(corsSource);

        // Como corsSource es UrlBasedCorsConfigurationSource, podemos hacer cast para inspeccionar
        var source = (org.springframework.web.cors.UrlBasedCorsConfigurationSource) corsSource;
        var corsConfig = source.getCorsConfigurations().get("/**");

        assertNotNull(corsConfig);
        assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:4300"));
        assertTrue(corsConfig.getAllowedMethods().contains("GET"));
        assertTrue(corsConfig.getAllowedHeaders().contains("Authorization"));
        assertTrue(corsConfig.getExposedHeaders().contains("Authorization"));
        assertTrue(corsConfig.getAllowCredentials());
    }

    @Test
    void securityFilterChain_buildsWithoutError() throws Exception {
        
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(null, null);
        SecurityConfig config = new SecurityConfig(jwtAuthFilter);

        
        HttpSecurity http = org.springframework.security.config.annotation.web.builders.HttpSecurityBuilder
                .newInstance();

        
        var securityFilterChain = config.securityFilterChain(http);

        assertNotNull(securityFilterChain);
    }
}
*/