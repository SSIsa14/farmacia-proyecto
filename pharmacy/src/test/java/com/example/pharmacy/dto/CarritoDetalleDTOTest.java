package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarritoDetalleDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        CarritoDetalleDTO dto = new CarritoDetalleDTO();

        dto.setIdCartItem(1L);
        dto.setIdMedicamento(101L);
        dto.setNombreMedicamento("Paracetamol");
        dto.setCantidad(2);
        dto.setPrecioUnitario(10.0);
        dto.setTotal(20.0);
        dto.setRequiereReceta("Sí");

        assertEquals(1L, dto.getIdCartItem());
        assertEquals(101L, dto.getIdMedicamento());
        assertEquals("Paracetamol", dto.getNombreMedicamento());
        assertEquals(2, dto.getCantidad());
        assertEquals(10.0, dto.getPrecioUnitario());
        assertEquals(20.0, dto.getTotal());
        assertEquals("Sí", dto.getRequiereReceta());
    }
}
