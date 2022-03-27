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
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        Double expected = 1000.12345;
        Double actual = cuenta.getSaldo().doubleValue();

        assertEquals(expected, actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta expected = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta actual   = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        assertEquals(expected, actual);
    }
}