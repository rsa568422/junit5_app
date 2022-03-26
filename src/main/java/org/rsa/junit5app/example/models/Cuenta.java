package org.rsa.junit5app.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Cuenta {

    private String persona;
    private BigDecimal saldo;

}
