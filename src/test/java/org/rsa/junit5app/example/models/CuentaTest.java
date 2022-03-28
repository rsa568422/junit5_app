package org.rsa.junit5app.example.models;

import org.junit.jupiter.api.*;
import org.rsa.junit5app.example.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    private Cuenta cuenta;

    @BeforeAll
    static void beforeAll() {
        System.out.println("iniciando la clase CuentaTest");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finalizando la clase CuentaTest");
    }

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));

        System.out.println("iniciando test sobre Cuenta");
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando test sobre Cuenta");
    }

    @Test
    @DisplayName("probando el nombre de la cuenta")
    void testNombreCuenta() {
        String expected = "Andres";

        String actual = this.cuenta.getPersona();

        assertAll(() -> assertNotNull(actual, () -> "la cuenta no puede ser nula"),
                  () -> assertEquals(expected, actual, () -> String.format("el nombre de la cuenta no es el que se esperaba, se esperaba %s y sin embargo fue %s", expected, actual)));
    }

    @Test
    @DisplayName("probando el saldo de la cuenta")
    void testSaldoCuenta() {
        Double expected = 1000.12345;
        Double actual = this.cuenta.getSaldo().doubleValue();

        assertAll(() -> assertNotNull(this.cuenta.getSaldo()),
                  () -> assertEquals(expected, actual),
                  () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
    }

    @Test
    @DisplayName("probando equals entre cuentas")
    void testReferenciaCuenta() {
        Cuenta expected = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta actual   = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("probando el debito de la cuenta")
    void testDebitoCuenta() {
        this.cuenta.debito(new BigDecimal(100));

        BigDecimal expected = new BigDecimal("900.12345");
        BigDecimal actual = this.cuenta.getSaldo();

        assertAll(() -> assertNotNull(actual),
                  () -> assertEquals(expected, actual),
                  () -> assertEquals(900, actual.intValue()),
                  () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
    }

    @Test
    @DisplayName("probando el crédito de la cuenta")
    void testCreditoCuenta() {
        this.cuenta.credito(new BigDecimal(100));

        BigDecimal expected = new BigDecimal("1100.12345");
        BigDecimal actual = this.cuenta.getSaldo();

        assertAll(() -> assertNotNull(actual),
                  () -> assertEquals(expected, actual),
                  () -> assertEquals(1100, actual.intValue()),
                  () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
    }

    @Test
    @DisplayName("probando error por saldo insuficiente")
    void testSaldoInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(SaldoInsuficienteException.class,
                                           () -> this.cuenta.debito(new BigDecimal(1500)));

        String expected = "Saldo insuficiente";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("probando transferencia de monto entre cuentas")
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");

        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()),
                  () -> assertEquals("3000", cuenta1.getSaldo().toPlainString()));
    }

    //@Disabled
    @Test
    @DisplayName("probando las relaciones entre el banco y sus cuentas")
    void testRelacionBancoCuentas() {
        //fail();
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