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

        assertAll(() -> assertNotNull(actual, () -> "la cuenta no puede ser nula"),
                  () -> assertEquals(expected, actual, () -> String.format("el nombre de la cuenta no es el que se esperaba, se esperaba %s y sin embargo fue %s", expected, actual)));
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        Double expected = 1000.12345;
        Double actual = cuenta.getSaldo().doubleValue();

        assertAll(() -> assertNotNull(cuenta.getSaldo()),
                  () -> assertEquals(expected, actual),
                  () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
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

        assertAll(() -> assertNotNull(actual),
                  () -> assertEquals(expected, actual),
                  () -> assertEquals(900, actual.intValue()),
                  () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));

        BigDecimal expected = new BigDecimal("1100.12345");
        BigDecimal actual = cuenta.getSaldo();

        assertAll(() -> assertNotNull(actual),
                  () -> assertEquals(expected, actual),
                  () -> assertEquals(1100, actual.intValue()),
                  () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
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

        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()),
                  () -> assertEquals("3000", cuenta1.getSaldo().toPlainString()));
    }

    @Test
    void testRelacionBancoCuentas() {
        String nombreBanco = "Banco del Estado";
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre(nombreBanco);
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> assertEquals("3000", cuenta1.getSaldo().toPlainString(), () -> "el valor del saldo de la cuenta 1 no es el esperado"),
                  () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(), () -> "el valor del saldo de la cuenta 2 no es el esperado"),
                  () -> assertEquals(2, banco.getCuentas().size(), () -> "la cantidad de cuentas asociadas al banco no es la esperada"),
                  () -> assertEquals(nombreBanco, cuenta1.getBanco().getNombre(), () -> "el nombre del banco asociado a la cuenta 1 no es el esperado"),
                  () -> assertEquals(nombreBanco, cuenta2.getBanco().getNombre(), () -> "el nombre del banco asociado a la cuenta 2 no es el esperado"),
                  () -> assertTrue(banco.getCuentas()
                                        .stream()
                                        .anyMatch(c -> c.getPersona().equals("John Doe")), () -> "el banco no contiene la cuenta de John Doe"),
                  () -> assertTrue(banco.getCuentas()
                                        .stream()
                                        .anyMatch(c -> c.getPersona().equals("Andres")), () -> "el banco no contiene la cuenta de Andres"));
    }

}