package org.rsa.junit5app.example.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.rsa.junit5app.example.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class Cuenta {

    private String persona;

    private BigDecimal saldo;

    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) {
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
    }

}
