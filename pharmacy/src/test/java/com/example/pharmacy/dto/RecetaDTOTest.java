package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        RecetaDTO receta = new RecetaDTO();

        Long idReceta = 1L;
        String codigoReceta = "RX12345";
        LocalDateTime fecha = LocalDateTime.now();
        Long idUsuario = 2L;
        String aprobadoSeguro = "Sí";
        String pdfUrl = "http://example.com/receta.pdf";

        RecetaDetalleDTO detalle1 = new RecetaDetalleDTO();
        detalle1.setIdDetalle(101L);
        RecetaDetalleDTO detalle2 = new RecetaDetalleDTO();
        detalle2.setIdDetalle(102L);
        List<RecetaDetalleDTO> detalles = Arrays.asList(detalle1, detalle2);

        receta.setIdReceta(idReceta);
        receta.setCodigoReceta(codigoReceta);
        receta.setFecha(fecha);
        receta.setIdUsuario(idUsuario);
        receta.setAprobadoSeguro(aprobadoSeguro);
        receta.setPdfUrl(pdfUrl);
        receta.setDetalles(detalles);

        assertEquals(idReceta, receta.getIdReceta());
        assertEquals(codigoReceta, receta.getCodigoReceta());
        assertEquals(fecha, receta.getFecha());
        assertEquals(idUsuario, receta.getIdUsuario());
        assertEquals(aprobadoSeguro, receta.getAprobadoSeguro());
        assertEquals(pdfUrl, receta.getPdfUrl());
        assertEquals(detalles, receta.getDetalles());
        assertEquals(2, receta.getDetalles().size());
        assertEquals(101L, receta.getDetalles().get(0).getIdDetalle());
    }
}
