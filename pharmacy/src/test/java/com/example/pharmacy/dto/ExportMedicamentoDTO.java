package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportMedicamentoDTOTest {

    @Test
    void constructorYGettersSetters_deberianFuncionarCorrectamente() {
        Long idMedicamento = 123L;
        Integer cantidad = 50;

        ExportMedicamentoDTO dto = new ExportMedicamentoDTO();
        dto.setIdMedicamento(idMedicamento);
        dto.setCantidad(cantidad);

        assertEquals(idMedicamento, dto.getIdMedicamento());
        assertEquals(cantidad, dto.getCantidad());
    }

    @Test
    void constructorConParametros_deberiaInicializarCorrectamente() {
        Long idMedicamento = 456L;
        Integer cantidad = 20;

        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(idMedicamento, cantidad);

        assertEquals(idMedicamento, dto.getIdMedicamento());
        assertEquals(cantidad, dto.getCantidad());
    }

    @Test
    void toString_deberiaContenerDatosEsperados() {
        ExportMedicamentoDTO dto = new ExportMedicamentoDTO(789L, 10);
        String toString = dto.toString();

        assertTrue(toString.contains("idMedicamento=789"));
        assertTrue(toString.contains("cantidad=10"));
    }
}
