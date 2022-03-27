package org.rsa.junit5app.example.models;

import org.junit.jupiter.api.Test;
import org.rsa.junit5app.example.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        String expected = "Andres";
        Cuenta cuenta = new Cuenta(expected, new BigDecimal("1000.12345"));

        String actual = cuenta.getPersona();

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        Double expected = 1000.12345;
        Double actual = cuenta.getSaldo().doubleValue();

        assertNotNull(cuenta.getSaldo());
        assertEquals(expected, actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta expected = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta actual   = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        assertEquals(expected, actual);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));

        BigDecimal expected = new BigDecimal("900.12345");
        BigDecimal actual = cuenta.getSaldo();

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(900, actual.intValue());
        assertEquals(expected.toPlainString(), actual.toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));

        BigDecimal expected = new BigDecimal("1100.12345");
        BigDecimal actual = cuenta.getSaldo();

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(1100, actual.intValue());
        assertEquals(expected.toPlainString(), actual.toPlainString());
    }

    @Test
    void testSaldoInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        Exception exception = assertThrows(SaldoInsuficienteException.class,
                                           () -> cuenta.debito(new BigDecimal(1500)));

        String expected = "Saldo insuficiente";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");

        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
        assertEquals(2, banco.getCuentas().size());
    }

}