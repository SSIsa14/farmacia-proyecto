package com.example.pharmacy.dto;

import com.example.pharmacy.model.Medicamento;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicamentoListWrapperTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        Medicamento medicamento1 = new Medicamento();
        medicamento1.setIdMedicamento(1L);
        medicamento1.setNombre("Paracetamol");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setIdMedicamento(2L);
        medicamento2.setNombre("Ibuprofeno");


        List<Medicamento> lista = Arrays.asList(medicamento1, medicamento2);

        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        wrapper.setMedicamentos(lista);

        List<Medicamento> resultado = wrapper.getMedicamentos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getNombre());
        assertEquals("Ibuprofeno", resultado.get(1).getNombre());
    }

    @Test
    void constructorConLista_deberiaAsignarMedicamentosCorrectamente() {
        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicamento(1L);
        medicamento.setNombre("Amoxicilina");


        List<Medicamento> lista = List.of(medicamento);
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper(lista);

        assertEquals(lista, wrapper.getMedicamentos());
    }
}
