package org.rsa.junit5app.example.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        String expected = "Andres";
        Cuenta cuenta = new Cuenta(expected, new BigDecimal("1000.12345"));

        String actual = cuenta.getPersona();

        assertEquals(expected, actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.valueOf(1000)) > 0);
    }

}