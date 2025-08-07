package com.example.pharmacy.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutDTOTest {

    @Test
    void gettersAndSetters_deberianFuncionarCorrectamente() {
        CheckoutDTO checkoutDTO = new CheckoutDTO();

        Long idCart = 5L;
        Double descuento = 15.0;
        String email = "usuario@correo.com";

        checkoutDTO.setIdCart(idCart);
        checkoutDTO.setDescuento(descuento);
        checkoutDTO.setEmail(email);

        assertEquals(idCart, checkoutDTO.getIdCart());
        assertEquals(descuento, checkoutDTO.getDescuento());
        assertEquals(email, checkoutDTO.getEmail());
    }
}
