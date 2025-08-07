/*
package com.example.pharmacy.config;

import com.example.pharmacy.dto.VentaDTO;
import com.example.pharmacy.dto.VentaDetalleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JdbcConfigurationTest {

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
    }

    @Test
    void updateCartStatus_deberiaRetornarTrueCuandoSeActualiza() {
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(1);

        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, 123L, "COMPLETADO");

        assertTrue(result);
        verify(jdbcTemplate, times(1)).update(anyString(), eq("COMPLETADO"), eq(123L));
    }

    @Test
    void updateCartStatus_deberiaRetornarFalseCuandoFallaLaActualizacion() {
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(0);

        boolean result = JdbcConfiguration.updateCartStatus(jdbcTemplate, 123L, "COMPLETADO");

        assertFalse(result);
    }

    @Test
    void insertVentaJdbc_deberiaInsertarVentaYRetornarId() {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdUsuario(10L);
        ventaDTO.setIdReceta(20L);
        ventaDTO.setTotal(100.0);
        ventaDTO.setImpuesto(10.0);
        ventaDTO.setDescuento(5.0);
        ventaDTO.setMontoPagado(105.0);

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA.NEXTVAL FROM DUAL", Long.class)).thenReturn(999L);
        when(jdbcTemplate.update(anyString(),
                eq(999L),
                eq(10L),
                eq(20L),
                any(),
                eq(100.0),
                eq(10.0),
                eq(5.0),
                eq(105.0))).thenReturn(1);

        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, ventaDTO);

        assertTrue((Boolean) result.get("success"));
        assertEquals(999L, result.get("idVenta"));
    }

    @Test
    void insertVentaJdbc_deberiaRetornarErrorCuandoFallaInsercion() {
        VentaDTO ventaDTO = new VentaDTO();
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenThrow(new RuntimeException("DB error"));

        Map<String, Object> result = JdbcConfiguration.insertVentaJdbc(jdbcTemplate, ventaDTO);

        assertFalse((Boolean) result.get("success"));
        assertEquals("DB error", result.get("error"));
    }

    @Test
    void insertVentaDetalleJdbc_deberiaInsertarDetalleYRetornarId() {
        VentaDetalleDTO detalleDTO = new VentaDetalleDTO();
        detalleDTO.setIdMedicamento(1L);
        detalleDTO.setCantidad(2);
        detalleDTO.setPrecioUnitario(50.0);
        detalleDTO.setTotalLinea(100.0);

        when(jdbcTemplate.queryForObject("SELECT SEQ_VENTA_DETALLE.NEXTVAL FROM DUAL", Long.class)).thenReturn(555L);
        when(jdbcTemplate.update(anyString(),
                eq(555L),
                eq(123L),
                eq(1L),
                eq(2),
                eq(50.0),
                eq(100.0))).thenReturn(1);

        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(jdbcTemplate, 123L, detalleDTO);

        assertTrue((Boolean) result.get("success"));
        assertEquals(555L, result.get("idVentaDetalle"));
    }

    @Test
    void insertVentaDetalleJdbc_deberiaRetornarErrorCuandoFallaInsercion() {
        VentaDetalleDTO detalleDTO = new VentaDetalleDTO();
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenThrow(new RuntimeException("DB error"));

        Map<String, Object> result = JdbcConfiguration.insertVentaDetalleJdbc(jdbcTemplate, 123L, detalleDTO);

        assertFalse((Boolean) result.get("success"));
        assertEquals("DB error", result.get("error"));
    }
}
*/