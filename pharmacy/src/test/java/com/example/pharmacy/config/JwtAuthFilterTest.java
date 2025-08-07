/*
package com.example.pharmacy.config;

import com.example.pharmacy.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtUtils jwtUtils;
    private ObjectMapper objectMapper;
    private JwtAuthFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        jwtUtils = mock(JwtUtils.class);
        objectMapper = new ObjectMapper();
        filter = new JwtAuthFilter(jwtUtils, objectMapper);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        // Limpia contexto de seguridad antes de cada test
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_callsFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_invalidAuthorizationHeader_callsFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic 12345");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws Exception {
        String token = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getCorreoFromToken(token)).thenReturn("user@example.com");
        when(jwtUtils.getRolFromToken(token)).thenReturn("USER");
        when(jwtUtils.getRolesFromToken(token)).thenReturn(List.of("USER", "ADMIN"));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("user@example.com", auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void doFilterInternal_invalidToken_sendsErrorResponse() throws Exception {
        String token = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateToken(token)).thenReturn(false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(outputStream));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(401);
        String jsonResponse = outputStream.toString();
        assertTrue(jsonResponse.contains("\"status\":401"));
        assertTrue(jsonResponse.contains("\"error\":\"unauthorized\""));
        assertTrue(jsonResponse.contains("\"message\":\"Token inválido\""));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_exceptionDuringProcessing_sendsErrorResponse() throws Exception {
        String token = "jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateToken(token)).thenThrow(new RuntimeException("error validating"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(outputStream));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(401);
        String jsonResponse = outputStream.toString();
        assertTrue(jsonResponse.contains("\"status\":401"));
        assertTrue(jsonResponse.contains("\"error\":\"unauthorized\""));
        assertTrue(jsonResponse.contains("\"message\":\"error validating\""));
        verify(filterChain, never()).doFilter(request, response);
    }

    // Helper class para simular ServletOutputStream desde un ByteArrayOutputStream
    static class DelegatingServletOutputStream extends jakarta.servlet.ServletOutputStream {
        private final ByteArrayOutputStream stream;

        DelegatingServletOutputStream(ByteArrayOutputStream stream) {
            this.stream = stream;
        }

        @Override
        public void write(int b) {
            stream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(jakarta.servlet.WriteListener listener) {
            // No implementado
        }
    }
}
*/