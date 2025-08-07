package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaValidadaDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        RecetaValidadaDTO dto = new RecetaValidadaDTO();

        String codigoReceta = "RX789";
        boolean valida = true;
        String paciente = "Juan Pérez";
        String doctor = "Dra. López";
        List<String> medicamentos = Arrays.asList("Paracetamol", "Ibuprofeno");
        String observaciones = "Paciente alérgico a la penicilina";

        dto.setCodigoReceta(codigoReceta);
        dto.setValida(valida);
        dto.setPaciente(paciente);
        dto.setDoctor(doctor);
        dto.setMedicamentos(medicamentos);
        dto.setObservaciones(observaciones);

        assertEquals(codigoReceta, dto.getCodigoReceta());
        assertTrue(dto.isValida());
        assertEquals(paciente, dto.getPaciente());
        assertEquals(doctor, dto.getDoctor());
        assertEquals(medicamentos, dto.getMedicamentos());
        assertEquals(observaciones, dto.getObservaciones());
    }

    @Test
    void toString_deberiaContenerDatosEsperados() {
        RecetaValidadaDTO dto = new RecetaValidadaDTO();
        dto.setCodigoReceta("RX123");
        dto.setValida(false);
        dto.setPaciente("Ana");
        dto.setDoctor("Dr. Martínez");
        dto.setMedicamentos(List.of("Amoxicilina"));
        dto.setObservaciones("Sin observaciones");

        String toString = dto.toString();

        assertTrue(toString.contains("codigoReceta='RX123'"));
        assertTrue(toString.contains("valida=false"));
        assertTrue(toString.contains("paciente='Ana'"));
        assertTrue(toString.contains("doctor='Dr. Martínez'"));
        assertTrue(toString.contains("medicamentos=[Amoxicilina]"));
        assertTrue(toString.contains("observaciones='Sin observaciones'"));
    }
}
