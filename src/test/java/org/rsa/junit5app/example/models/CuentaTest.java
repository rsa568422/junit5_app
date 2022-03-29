package org.rsa.junit5app.example.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.rsa.junit5app.example.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

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

    @Nested
    @DisplayName("probando atributos de la cuenta")
    class CuentaNombreSaldo {
        @Test
        @DisplayName("nombre de la cuenta")
        void testNombreCuenta() {
            String expected = "Andres";

            String actual = cuenta.getPersona();

            assertAll(() -> assertNotNull(actual, () -> "la cuenta no puede ser nula"),
                    () -> assertEquals(expected, actual, () -> String.format("el nombre de la cuenta no es el que se esperaba, se esperaba %s y sin embargo fue %s", expected, actual)));
        }

        @Test
        @DisplayName("saldo de la cuenta")
        void testSaldoCuenta() {
            Double expected = 1000.12345;
            Double actual = cuenta.getSaldo().doubleValue();

            assertAll(() -> assertNotNull(cuenta.getSaldo()),
                    () -> assertEquals(expected, actual),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
        }

        @Test
        @DisplayName("equals entre cuentas")
        void testReferenciaCuenta() {
            Cuenta expected = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            Cuenta actual   = new Cuenta("John Doe", new BigDecimal("8900.9997"));

            assertEquals(expected, actual);
        }
    }

    @Nested
    class CuentaOperaciones {
        @Test
        @DisplayName("probando el debito de la cuenta")
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));

            BigDecimal expected = new BigDecimal("900.12345");
            BigDecimal actual = cuenta.getSaldo();

            assertAll(() -> assertNotNull(actual),
                    () -> assertEquals(expected, actual),
                    () -> assertEquals(900, actual.intValue()),
                    () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
        }

        @Test
        @DisplayName("probando el crédito de la cuenta")
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));

            BigDecimal expected = new BigDecimal("1100.12345");
            BigDecimal actual = cuenta.getSaldo();

            assertAll(() -> assertNotNull(actual),
                    () -> assertEquals(expected, actual),
                    () -> assertEquals(1100, actual.intValue()),
                    () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
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

    @Nested
    class SistemaOperativo {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {

        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
            fail();
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
            fail();
        }
    }

    @Nested
    class JavaVersion {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
            fail();
        }

        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void testSoloJdk11() {

        }

        @Test
        @DisabledOnJre(JRE.JAVA_11)
        void testNoJdk11() {
            fail();
        }
    }

    @Nested
    class SystemProperties {
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((key, value) -> System.out.printf("%s:%s%n", key, value));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "11.*.*")
        void testJavaVersion() {

        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {

        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64() {
            fail();
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "Roberto")
        void testSoloRoberto() {

        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {

        }
    }

    @Nested
    class EnvironmentVariable {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> env = System.getenv();
            env.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.0.2.*")
        void testJavaHome() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "12")
        void testProcesadores() {
            fail();
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
            fail();
        }
    }

    @Test
    @DisplayName("testSaldoCuentaDev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        Double expected = 1000.12345;
        Double actual = this.cuenta.getSaldo().doubleValue();
        assumeTrue(esDev);
        assertAll(() -> assertNotNull(this.cuenta.getSaldo()),
                () -> assertEquals(expected, actual),
                () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
    }

    @Test
    @DisplayName("testSaldoCuentaDev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        Double expected = 1000.12345;
        Double actual = this.cuenta.getSaldo().doubleValue();
        assumingThat(esDev, () -> {
            assertAll(() -> assertNotNull(this.cuenta.getSaldo()),
                    () -> assertEquals(expected, actual));
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @RepeatedTest(value = 5, name = "{displayName}: {currentRepetition} de {totalRepetitions}")
    @DisplayName("pruebas con repeticiones")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3) {
            System.out.printf("estamos en la repetición %d\n", info.getCurrentRepetition());
        }

        cuenta.debito(new BigDecimal(100));

        BigDecimal expected = new BigDecimal("900.12345");
        BigDecimal actual = cuenta.getSaldo();

        assertAll(() -> assertNotNull(actual),
                () -> assertEquals(expected, actual),
                () -> assertEquals(900, actual.intValue()),
                () -> assertEquals(expected.toPlainString(), actual.toPlainString()));
    }

    @Nested
    class PruebasParametrizadasTest {
        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "750", "1000.1234"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));

            BigDecimal actual = cuenta.getSaldo();

            assertAll(() -> assertNotNull(actual),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1, 100", "2, 200", "3, 300", "4, 500", "5, 750", "6, 1000.1234"})
        void testDebitoCuentaCsvResource(String index, String monto) {
            cuenta.debito(new BigDecimal(monto));

            BigDecimal actual = cuenta.getSaldo();

            assertAll(() -> assertNotNull(actual),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"200, 100, John, Andres", "250, 200, Pepe, Pepe"})
        void testDebitoCuentaCsvResource2(String saldo, String monto, String expected, String actual) {
            System.out.printf("%s -> %s\n", saldo, monto);
            cuenta.setPersona(actual);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));

            assertAll(() -> assertNotNull(cuenta.getSaldo()),
                    () -> assertNotNull(cuenta.getPersona()),
                    () -> assertEquals(expected, actual),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));

            BigDecimal actual = cuenta.getSaldo();

            assertAll(() -> assertNotNull(actual),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCsvFileSource2(String saldo, String monto, String expected, String actual) {
            System.out.printf("%s -> %s\n", saldo, monto);
            cuenta.setPersona(actual);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));

            assertAll(() -> assertNotNull(cuenta.getSaldo()),
                    () -> assertNotNull(cuenta.getPersona()),
                    () -> assertEquals(expected, actual),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
        }
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));

        BigDecimal actual = cuenta.getSaldo();

        assertAll(() -> assertNotNull(actual),
                () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0));
    }

    private static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "750", "1000.1234");
    }

}