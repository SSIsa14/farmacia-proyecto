/*
package com.example.pharmacy.controllers;

import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.service.UsuarioService;
import com.example.pharmacy.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RolRepository rolRepository;

    private Usuario exampleUser;

    @BeforeEach
    void setUp() {
        exampleUser = new Usuario();
        exampleUser.setIdUsuario(1L);
        exampleUser.setCorreo("test@example.com");
        exampleUser.setNombre("Test User");
        exampleUser.setPasswordHash("hashedPassword");
        exampleUser.setActivo(true);
        exampleUser.setPerfilCompleto(true);
        exampleUser.setPrimerLogin(true);
    }

    // --- Register Tests ---
    @Test
    void register_success_withRole() throws Exception {
        when(usuarioService.register(any())).thenReturn(exampleUser);
        when(rolRepository.existsById(5L)).thenReturn(true);
        doNothing().when(usuarioService).assignRolesToUser(exampleUser.getIdUsuario(), List.of(5L));
        when(usuarioService.getUserRoles(exampleUser.getIdUsuario())).thenReturn(List.of("ADMIN"));
        when(jwtUtils.generateToken(eq(exampleUser.getCorreo()), anyString(), anyList())).thenReturn("token123");

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "password123",
                "nombre": "Test User",
                "rol": "5"
            }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("exitosamente")))
                .andExpect(jsonPath("$.correo", is("test@example.com")))
                .andExpect(jsonPath("$.rol", is("ADMIN")))
                .andExpect(jsonPath("$.roles[0]", is("ADMIN")))
                .andExpect(jsonPath("$.token", is("token123")));
    }

    @Test
    void register_fails_missingEmailOrPassword() throws Exception {
        String jsonBody = """
            {
                "correo": "",
                "password": ""
            }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Correo y contraseña requeridos")));
    }

    @Test
    void register_assignRoleFailsButStillRegisters() throws Exception {
        when(usuarioService.register(any())).thenReturn(exampleUser);
        when(rolRepository.existsById(99L)).thenReturn(false);
        when(usuarioService.getUserRoles(exampleUser.getIdUsuario())).thenReturn(List.of("INVITADO"));
        when(jwtUtils.generateToken(anyString(), anyString(), anyList())).thenReturn("tokenABC");

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "password123",
                "rol": "99"
            }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol", is("INVITADO")))
                .andExpect(jsonPath("$.token", is("tokenABC")));
    }

    // --- Login Tests ---
    @Test
    void login_success_profileComplete() throws Exception {
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(passwordEncoder.matches("rawPassword", exampleUser.getPasswordHash())).thenReturn(true);
        when(usuarioService.getUserRoles(exampleUser.getIdUsuario())).thenReturn(List.of("ADMIN"));
        when(jwtUtils.generateToken("test@example.com", "ADMIN", List.of("ADMIN"))).thenReturn("tokenXYZ");
        doNothing().when(usuarioService).updateFirstLogin(exampleUser.getIdUsuario());

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "rawPassword"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("tokenXYZ")))
                .andExpect(jsonPath("$.user.correo", is("test@example.com")))
                .andExpect(jsonPath("$.user.perfilCompleto", is("Y")))
                .andExpect(jsonPath("$.user.primerLogin", is("Y")));
    }

    @Test
    void login_fails_invalidCredentials() throws Exception {
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(passwordEncoder.matches("wrongPass", exampleUser.getPasswordHash())).thenReturn(false);

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "wrongPass"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", containsString("Credenciales inválidas")));
    }

    @Test
    void login_fails_userInactive() throws Exception {
        exampleUser.setActivo(false);
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(passwordEncoder.matches("rawPassword", exampleUser.getPasswordHash())).thenReturn(true);

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "rawPassword"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", containsString("Usuario inactivo")));
    }

    @Test
    void login_fails_noRolesAssigned() throws Exception {
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(passwordEncoder.matches("rawPassword", exampleUser.getPasswordHash())).thenReturn(true);
        when(usuarioService.getUserRoles(exampleUser.getIdUsuario())).thenReturn(List.of());

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "rawPassword"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", containsString("Usuario sin roles asignados")))
                .andExpect(jsonPath("$.requiresAction", is("AWAIT_ROLE_ASSIGNMENT")));
    }

    @Test
    void login_profileIncomplete_requiresCompleteProfile() throws Exception {
        exampleUser.setPerfilCompleto(false);
        exampleUser.setPrimerLogin(false);
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(passwordEncoder.matches("rawPassword", exampleUser.getPasswordHash())).thenReturn(true);
        when(usuarioService.getUserRoles(exampleUser.getIdUsuario())).thenReturn(List.of("USER"));
        when(jwtUtils.generateToken("test@example.com", "USER", List.of("USER"))).thenReturn("tokenIncomplete");

        String jsonBody = """
            {
                "correo": "test@example.com",
                "password": "rawPassword"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requiresAction", is("COMPLETE_PROFILE")))
                .andExpect(jsonPath("$.token", is("tokenIncomplete")))
                .andExpect(jsonPath("$.user.perfilCompleto", is("N")));
    }

    // --- Complete Profile Tests ---
    @Test
    void completeProfile_success() throws Exception {
        exampleUser.setNombre("Old Name");
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(usuarioService.completeProfile(exampleUser.getIdUsuario())).thenReturn(true);
        when(usuarioService.getUserRoles(exampleUser.getIdUsuario())).thenReturn(List.of("ADMIN"));
        when(jwtUtils.generateToken("test@example.com", "ADMIN", List.of("ADMIN"))).thenReturn("tokenComplete");

        String jsonBody = """
            {
                "correo": "test@example.com",
                "nombre": "New Name"
            }
        """;

        mockMvc.perform(post("/api/auth/complete-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Perfil completado exitosamente")))
                .andExpect(jsonPath("$.user.nombre", is("New Name")))
                .andExpect(jsonPath("$.perfilCompleto", is("Y")))
                .andExpect(jsonPath("$.token", is("tokenComplete")));
    }

    @Test
    void completeProfile_fails_missingCorreo() throws Exception {
        String jsonBody = """
            {
                "nombre": "New Name"
            }
        """;

        mockMvc.perform(post("/api/auth/complete-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Correo requerido")));
    }

    @Test
    void completeProfile_fails_missingNombre() throws Exception {
        String jsonBody = """
            {
                "correo": "test@example.com"
            }
        """;

        mockMvc.perform(post("/api/auth/complete-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Nombre requerido")));
    }

    @Test
    void completeProfile_fails_completeProfileFalse() throws Exception {
        when(usuarioService.findByCorreo("test@example.com")).thenReturn(exampleUser);
        when(usuarioService.completeProfile(exampleUser.getIdUsuario())).thenReturn(false);

        String jsonBody = """
            {
                "correo": "test@example.com",
                "nombre": "New Name"
            }
        """;

        mockMvc.perform(post("/api/auth/complete-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("No se pudo completar el perfil")));
    }
}
*/