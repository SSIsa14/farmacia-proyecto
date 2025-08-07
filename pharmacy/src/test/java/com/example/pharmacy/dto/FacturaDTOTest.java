package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FacturaDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        FacturaDTO factura = new FacturaDTO();

        Long idFactura = 1001L;
        Long idVenta = 2002L;
        VentaDTO venta = new VentaDTO(); // Puedes usar mocks o instancias reales simples
        LocalDateTime fecha = LocalDateTime.now();
        Double total = 99.99;
        String pdfUrl = "http://ejemplo.com/factura.pdf";

        factura.setIdFactura(idFactura);
        factura.setIdVenta(idVenta);
        factura.setVenta(venta);
        factura.setFechaFactura(fecha);
        factura.setTotalFactura(total);
        factura.setPdfUrl(pdfUrl);

        assertEquals(idFactura, factura.getIdFactura());
        assertEquals(idVenta, factura.getIdVenta());
        assertEquals(venta, factura.getVenta());
        assertEquals(fecha, factura.getFechaFactura());
        assertEquals(total, factura.getTotalFactura());
        assertEquals(pdfUrl, factura.getPdfUrl());
    }
}
