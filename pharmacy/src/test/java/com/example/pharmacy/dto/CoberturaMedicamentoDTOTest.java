package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoberturaMedicamentoDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        CoberturaMedicamentoDTO dto = new CoberturaMedicamentoDTO();

        String numeroAfiliacion = "123456";
        String codigoMedicamento = "MED001";
        String nombre = "Paracetamol";
        boolean cubierto = true;
        double montoAutorizado = 100.50;
        double copago = 25.00;
        String mensaje = "Cobertura total";

        dto.setNumeroAfiliacion(numeroAfiliacion);
        dto.setCodigoMedicamento(codigoMedicamento);
        dto.setNombre(nombre);
        dto.setCubierto(cubierto);
        dto.setMontoAutorizado(montoAutorizado);
        dto.setCopago(copago);
        dto.setMensaje(mensaje);

        assertEquals(numeroAfiliacion, dto.getNumeroAfiliacion());
        assertEquals(codigoMedicamento, dto.getCodigoMedicamento());
        assertEquals(nombre, dto.getNombre());
        assertTrue(dto.isCubierto());
        assertEquals(montoAutorizado, dto.getMontoAutorizado());
        assertEquals(copago, dto.getCopago());
        assertEquals(mensaje, dto.getMensaje());
    }

    @Test
    void toString_deberiaContenerDatosEsperados() {
        CoberturaMedicamentoDTO dto = new CoberturaMedicamentoDTO();
        dto.setNumeroAfiliacion("789101");
        dto.setCodigoMedicamento("MED999");
        dto.setNombre("Ibuprofeno");
        dto.setCubierto(false);
        dto.setMontoAutorizado(50.0);
        dto.setCopago(10.0);
        dto.setMensaje("No cubierto");

        String toString = dto.toString();

        assertTrue(toString.contains("numeroAfiliacion='789101'"));
        assertTrue(toString.contains("codigoMedicamento='MED999'"));
        assertTrue(toString.contains("nombre='Ibuprofeno'"));
        assertTrue(toString.contains("cubierto=false"));
        assertTrue(toString.contains("montoAutorizado=50.0"));
        assertTrue(toString.contains("copago=10.0"));
        assertTrue(toString.contains("mensaje='No cubierto'"));
    }
}
