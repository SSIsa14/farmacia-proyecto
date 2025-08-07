/*
package com.example.pharmacy.controllers;

import com.example.pharmacy.dto.UserDTO;
import com.example.pharmacy.model.Rol;
import com.example.pharmacy.model.Usuario;
import com.example.pharmacy.repository.RolRepository;
import com.example.pharmacy.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private RolRepository rolRepository;

    private UserDTO exampleUser;
    private Usuario exampleUsuario;

    @BeforeEach
    void setUp() {
        exampleUser = new UserDTO();
        exampleUser.setId(1L);
        exampleUser.setCorreo("test@example.com");
        exampleUser.setNombre("Test User");

        // Creamos el modelo Usuario que retorna findByCorreo
        exampleUsuario = new Usuario();
        exampleUsuario.setCorreo("test@example.com");
        // Asigna otros campos mínimos necesarios si aplica
    }

    @Test
    void getAllUsers_returnsUserList() throws Exception {
        when(usuarioService.getAllUsers()).thenReturn(List.of(exampleUser));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(exampleUser.getId()))
                .andExpect(jsonPath("$[0].correo").value(exampleUser.getCorreo()))
                .andExpect(jsonPath("$[0].nombre").value(exampleUser.getNombre()));
    }

    @Test
    void filterUsers_returnsFilteredUsers() throws Exception {
        when(usuarioService.findUsersByFilters(anyString(), any(), any(), anyString()))
                .thenReturn(List.of(exampleUser));

        mockMvc.perform(get("/api/admin/users/filter")
                        .param("email", "test@example.com")
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correo", is(exampleUser.getCorreo())));
    }

    @Test
    void getUserById_found() throws Exception {
        when(usuarioService.findByCorreo(anyString())).thenReturn(exampleUsuario);  // modelo
        when(usuarioService.getMyProfile(anyString())).thenReturn(exampleUser);      // dto

        mockMvc.perform(get("/api/admin/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value(exampleUser.getCorreo()));
    }

    @Test
    void getUserById_notFound() throws Exception {
        when(usuarioService.findByCorreo(anyString())).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/admin/users/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void activateUser_success() throws Exception {
        when(usuarioService.activateUser(eq(1L), eq(2L))).thenReturn(true);

        String jsonBody = "{\"roleId\":2}";

        mockMvc.perform(post("/api/admin/users/{id}/activate", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User activated successfully")));
    }

    @Test
    void activateUser_missingRoleId() throws Exception {
        String jsonBody = "{}";

        mockMvc.perform(post("/api/admin/users/{id}/activate", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Role ID is required")));
    }

    @Test
    void deactivateUser_success() throws Exception {
        when(usuarioService.deactivateUser(1L)).thenReturn(true);

        mockMvc.perform(post("/api/admin/users/{id}/deactivate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User deactivated successfully")));
    }

    @Test
    void assignRoles_success() throws Exception {
        doNothing().when(usuarioService).assignRolesToUser(eq(1L), anyList());

        String jsonBody = "{\"roleIds\":[1,2]}";

        mockMvc.perform(post("/api/admin/users/{id}/roles", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Roles assigned successfully")));
    }

    @Test
    void assignRoles_missingRoleIds() throws Exception {
        String jsonBody = "{}";

        mockMvc.perform(post("/api/admin/users/{id}/roles", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Role IDs are required")));
    }

    @Test
    void removeRole_success() throws Exception {
        doNothing().when(usuarioService).removeRolFromUser(1L, 2L);

        mockMvc.perform(delete("/api/admin/users/{userId}/roles/{roleId}", 1L, 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Role removed successfully")));
    }

    @Test
    void getAllRoles_returnsRoles() throws Exception {
        Rol rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("ADMIN");

        Rol rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("USER");

        when(rolRepository.findAll()).thenReturn(List.of(rol1, rol2));

        mockMvc.perform(get("/api/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("ADMIN")))
                .andExpect(jsonPath("$[1].nombre", is("USER")));
    }
}
*/