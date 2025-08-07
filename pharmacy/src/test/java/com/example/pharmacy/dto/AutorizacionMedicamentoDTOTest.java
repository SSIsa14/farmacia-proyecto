package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutorizacionMedicamentoDTOTest {

    @Test
    void gettersAndSetters_deberianGuardarYRetornarLosValoresCorrectos() {
        AutorizacionMedicamentoDTO dto = new AutorizacionMedicamentoDTO();

        dto.setAutorizacion("A12345");
        dto.setMontoAutorizado(150.75);
        dto.setCopago(20.00);
        dto.setEstado("APROBADO");
        dto.setMensaje("Autorizado correctamente");

        assertEquals("A12345", dto.getAutorizacion());
        assertEquals(150.75, dto.getMontoAutorizado());
        assertEquals(20.00, dto.getCopago());
        assertEquals("APROBADO", dto.getEstado());
        assertEquals("Autorizado correctamente", dto.getMensaje());
    }

    @Test
    void toString_deberiaIncluirTodosLosCampos() {
        AutorizacionMedicamentoDTO dto = new AutorizacionMedicamentoDTO();
        dto.setAutorizacion("A12345");
        dto.setMontoAutorizado(100.0);
        dto.setCopago(10.0);
        dto.setEstado("PENDIENTE");
        dto.setMensaje("Esperando validación");

        String toString = dto.toString();
        assertTrue(toString.contains("A12345"));
        assertTrue(toString.contains("100.0"));
        assertTrue(toString.contains("10.0"));
        assertTrue(toString.contains("PENDIENTE"));
        assertTrue(toString.contains("Esperando validación"));
    }
}
