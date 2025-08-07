package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        UserDTO user = new UserDTO();

        Long id = 1L;
        String nombre = "Sofía";
        String correo = "sofi@example.com";
        String rol = "ADMIN";
        List<String> roles = Arrays.asList("ADMIN", "USER");
        String activo = "Y";
        String password = "secreto";
        boolean perfilCompleto = true;
        boolean primerLogin = false;
        LocalDateTime fechaCreacion = LocalDateTime.now();

        user.setId(id);
        user.setNombre(nombre);
        user.setCorreo(correo);
        user.setRol(rol);
        user.setRoles(roles);
        user.setActivo(activo);
        user.setPassword(password);
        user.setPerfilCompleto(perfilCompleto);
        user.setPrimerLogin(primerLogin);
        user.setFechaCreacion(fechaCreacion);

        assertEquals(id, user.getId());
        assertEquals(nombre, user.getNombre());
        assertEquals(correo, user.getCorreo());
        assertEquals(rol, user.getRol());
        assertEquals(roles, user.getRoles());
        assertEquals(activo, user.getActivo());
        assertEquals(password, user.getPassword());
        assertTrue(user.isPerfilCompleto());
        assertFalse(user.isPrimerLogin());
        assertEquals(fechaCreacion, user.getFechaCreacion());
    }
}
