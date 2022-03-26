package org.rsa.junit5app.example.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Cuenta {

    private String persona;
    private BigDecimal saldo;

}
