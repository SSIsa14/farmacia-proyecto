package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VentaDetalleDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        VentaDetalleDTO dto = new VentaDetalleDTO();

        dto.setIdVentaDetalle(100L);
        dto.setIdMedicamento(200L);
        dto.setCantidad(5);
        dto.setPrecioUnitario(10.0);
        dto.setTotalLinea(50.0);

        assertEquals(100L, dto.getIdVentaDetalle());
        assertEquals(200L, dto.getIdMedicamento());
        assertEquals(5, dto.getCantidad());
        assertEquals(10.0, dto.getPrecioUnitario());
        assertEquals(50.0, dto.getTotalLinea());
    }

    @Test
    void isCantidadValida_deberiaRetornarTrueCuandoCantidadMayorQueCero() {
        VentaDetalleDTO dto = new VentaDetalleDTO();
        dto.setCantidad(3);
        assertTrue(dto.isCantidadValida());
    }

    @Test
    void isCantidadValida_deberiaRetornarFalseCuandoCantidadEsNull() {
        VentaDetalleDTO dto = new VentaDetalleDTO();
        dto.setCantidad(null);
        assertFalse(dto.isCantidadValida());
    }

    @Test
    void isCantidadValida_deberiaRetornarFalseCuandoCantidadEsCero() {
        VentaDetalleDTO dto = new VentaDetalleDTO();
        dto.setCantidad(0);
        assertFalse(dto.isCantidadValida());
    }

    @Test
    void toString_deberiaContenerDatosEsperados() {
        VentaDetalleDTO dto = new VentaDetalleDTO();
        dto.setIdVentaDetalle(1L);
        dto.setIdMedicamento(2L);
        dto.setCantidad(4);
        dto.setPrecioUnitario(15.5);
        dto.setTotalLinea(62.0);

        String str = dto.toString();

        assertTrue(str.contains("idVentaDetalle=1"));
        assertTrue(str.contains("idMedicamento=2"));
        assertTrue(str.contains("cantidad=4"));
        assertTrue(str.contains("precioUnitario=15.5"));
        assertTrue(str.contains("totalLinea=62.0"));
    }
}
