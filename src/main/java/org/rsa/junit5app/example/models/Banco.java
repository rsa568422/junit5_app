package org.rsa.junit5app.example.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Banco {

    private String nombre;

    private List<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new LinkedList<>();
    }

    public void addCuenta(Cuenta cuenta) {
        cuenta.setBanco(this);
        this.cuentas.add(cuenta);
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
        origen.debito(monto);
        destino.credito(monto);
    }

}
