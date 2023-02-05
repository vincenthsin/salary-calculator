package com.idiosoul.salarycalculator.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PayrollSlip {
    private BigDecimal grossBaseSalary;
    private BigDecimal siBaseAmount;
}
