package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        VentaDTO venta = new VentaDTO();

        venta.setIdVenta(1L);
        venta.setIdUsuario(2L);
        venta.setIdReceta(3L);
        venta.setCodigoReceta("REC-ABC123");
        venta.setNumeroAfiliacion("AFI-XYZ789");
        LocalDateTime fecha = LocalDateTime.now();
        venta.setFechaVenta(fecha);
        venta.setTotal(150.0);
        venta.setImpuesto(15.0);
        venta.setDescuento(10.0);
        venta.setMontoPagado(155.0);

        VentaDetalleDTO detalle = new VentaDetalleDTO();
        detalle.setCantidad(2);
        venta.setDetalles(List.of(detalle));

        assertEquals(1L, venta.getIdVenta());
        assertEquals(2L, venta.getIdUsuario());
        assertEquals(3L, venta.getIdReceta());
        assertEquals("REC-ABC123", venta.getCodigoReceta());
        assertEquals("AFI-XYZ789", venta.getNumeroAfiliacion());
        assertEquals(fecha, venta.getFechaVenta());
        assertEquals(150.0, venta.getTotal());
        assertEquals(15.0, venta.getImpuesto());
        assertEquals(10.0, venta.getDescuento());
        assertEquals(155.0, venta.getMontoPagado());
        assertNotNull(venta.getDetalles());
        assertEquals(1, venta.getDetalles().size());
        assertEquals(2, venta.getDetalles().get(0).getCantidad());
    }

    @Test
    void isCodigoRecetaValido_deberiaRetornarTrueParaFormatoCorrecto() {
        VentaDTO venta = new VentaDTO();
        venta.setCodigoReceta("REC-123ABC");
        assertTrue(venta.isCodigoRecetaValido());
    }

    @Test
    void isCodigoRecetaValido_deberiaRetornarFalseParaFormatoIncorrecto() {
        VentaDTO venta = new VentaDTO();
        venta.setCodigoReceta("123-REC");
        assertFalse(venta.isCodigoRecetaValido());

        venta.setCodigoReceta(null);
        assertFalse(venta.isCodigoRecetaValido());

        venta.setCodigoReceta("REC_123");
        assertFalse(venta.isCodigoRecetaValido());
    }

    @Test
    void isNumeroAfiliacionValido_deberiaRetornarTrueParaFormatoCorrecto() {
        VentaDTO venta = new VentaDTO();
        venta.setNumeroAfiliacion("AFI-456XYZ");
        assertTrue(venta.isNumeroAfiliacionValido());
    }

    @Test
    void isNumeroAfiliacionValido_deberiaRetornarFalseParaFormatoIncorrecto() {
        VentaDTO venta = new VentaDTO();
        venta.setNumeroAfiliacion("XYZ-AFI");
        assertFalse(venta.isNumeroAfiliacionValido());

        venta.setNumeroAfiliacion(null);
        assertFalse(venta.isNumeroAfiliacionValido());

        venta.setNumeroAfiliacion("AFI_123");
        assertFalse(venta.isNumeroAfiliacionValido());
    }

    @Test
    void isDetallesValidos_deberiaRetornarTrueConDetallesValidos() {
        VentaDTO venta = new VentaDTO();
        VentaDetalleDTO d1 = new VentaDetalleDTO();
        d1.setCantidad(1);
        VentaDetalleDTO d2 = new VentaDetalleDTO();
        d2.setCantidad(2);
        venta.setDetalles(List.of(d1, d2));

        assertTrue(venta.isDetallesValidos());
    }

    @Test
    void isDetallesValidos_deberiaRetornarFalseSiDetallesEsNull() {
        VentaDTO venta = new VentaDTO();
        venta.setDetalles(null);
        assertFalse(venta.isDetallesValidos());
    }

    @Test
    void isDetallesValidos_deberiaRetornarFalseSiDetallesEstaVacio() {
        VentaDTO venta = new VentaDTO();
        venta.setDetalles(List.of());
        assertFalse(venta.isDetallesValidos());
    }

    @Test
    void isDetallesValidos_deberiaRetornarFalseSiAlgunaCantidadEsNull() {
        VentaDTO venta = new VentaDTO();
        VentaDetalleDTO d = new VentaDetalleDTO();
        d.setCantidad(null);
        venta.setDetalles(List.of(d));
        assertFalse(venta.isDetallesValidos());
    }

    @Test
    void isDetallesValidos_deberiaRetornarFalseSiAlgunaCantidadEsCero() {
        VentaDTO venta = new VentaDTO();
        VentaDetalleDTO d = new VentaDetalleDTO();
        d.setCantidad(0);
        venta.setDetalles(List.of(d));
        assertFalse(venta.isDetallesValidos());
    }

    @Test
    void toString_deberiaContenerDatosEsperados() {
        VentaDTO venta = new VentaDTO();
        venta.setIdVenta(10L);
        venta.setCodigoReceta("REC-001");
        venta.setNumeroAfiliacion("AFI-002");

        String str = venta.toString();

        assertTrue(str.contains("idVenta=10"));
        assertTrue(str.contains("codigoReceta='REC-001'"));
        assertTrue(str.contains("numeroAfiliacion='AFI-002'"));
    }
}
