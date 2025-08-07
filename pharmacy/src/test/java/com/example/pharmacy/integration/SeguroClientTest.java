package com.example.pharmacy.integration;

import com.example.pharmacy.dto.CoberturaMedicamentoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeguroClientTest {

    private final SeguroClient seguroClient = new SeguroClient();

    @Test
    void validarMedicamento_coberturaValida() {
        String numeroAfiliacion = "AFI-9999";
        String codigoMedicamento = "1";

        CoberturaMedicamentoDTO resultado = seguroClient.validarMedicamento(numeroAfiliacion, codigoMedicamento);

        assertNotNull(resultado);
        assertEquals(numeroAfiliacion, resultado.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, resultado.getCodigoMedicamento());
        assertTrue(resultado.isCubierto());
        assertEquals(0.0, resultado.getMontoAutorizado());
        assertEquals(0.0, resultado.getCopago());
        assertEquals("Cobertura 80% para este medicamento.", resultado.getMensaje());
    }

}
