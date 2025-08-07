package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarritoDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        CarritoDTO carritoDTO = new CarritoDTO();

        Long idCart = 1L;
        Long idUsuario = 99L;
        String status = "PENDIENTE";
        LocalDateTime fechaCreacion = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaActualizacion = LocalDateTime.now();

        CarritoDetalleDTO item = new CarritoDetalleDTO();
        item.setIdCartItem(10L);
        item.setNombreMedicamento("Ibuprofeno");

        List<CarritoDetalleDTO> items = List.of(item);
        Double total = 50.0;

        carritoDTO.setIdCart(idCart);
        carritoDTO.setIdUsuario(idUsuario);
        carritoDTO.setStatus(status);
        carritoDTO.setFechaCreacion(fechaCreacion);
        carritoDTO.setFechaActualizacion(fechaActualizacion);
        carritoDTO.setItems(items);
        carritoDTO.setTotal(total);

        assertEquals(idCart, carritoDTO.getIdCart());
        assertEquals(idUsuario, carritoDTO.getIdUsuario());
        assertEquals(status, carritoDTO.getStatus());
        assertEquals(fechaCreacion, carritoDTO.getFechaCreacion());
        assertEquals(fechaActualizacion, carritoDTO.getFechaActualizacion());
        assertEquals(items, carritoDTO.getItems());
        assertEquals(total, carritoDTO.getTotal());
    }
}
