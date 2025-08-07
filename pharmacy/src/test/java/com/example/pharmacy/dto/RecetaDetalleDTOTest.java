package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecetaDetalleDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        RecetaDetalleDTO dto = new RecetaDetalleDTO();

        dto.setIdDetalle(1L);
        dto.setIdMedicamento(10L);
        dto.setDosis("500mg");
        dto.setFrecuencia("Cada 8 horas");
        dto.setDuracion("7 días");
        dto.setCantidadRequerida(21);
        dto.setObservaciones("Tomar con comida");

        assertEquals(1L, dto.getIdDetalle());
        assertEquals(10L, dto.getIdMedicamento());
        assertEquals("500mg", dto.getDosis());
        assertEquals("Cada 8 horas", dto.getFrecuencia());
        assertEquals("7 días", dto.getDuracion());
        assertEquals(21, dto.getCantidadRequerida());
        assertEquals("Tomar con comida", dto.getObservaciones());
    }
}
