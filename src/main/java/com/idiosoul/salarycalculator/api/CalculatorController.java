package com.idiosoul.salarycalculator.api;

import com.idiosoul.salarycalculator.model.SalaryFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CalculatorController {
    @GetMapping("/salary")
    public SalaryFlow calculate(BigDecimal grossBaseSalary) {
        BigDecimal ylaobxf = grossBaseSalary.multiply(BigDecimal.valueOf(8.0 / 100));
        BigDecimal yliaobxf = grossBaseSalary.multiply(BigDecimal.valueOf(2.0 / 100));
        BigDecimal sybxf = grossBaseSalary.multiply(BigDecimal.valueOf(0.5 / 100));
        BigDecimal zfgjj = grossBaseSalary.multiply(BigDecimal.valueOf(7.0 / 100));

        BigDecimal pendingTaxAmount = grossBaseSalary.multiply(BigDecimal.valueOf(1 - 10.5 / 100));
        BigDecimal grsds = pendingTaxAmount.multiply(BigDecimal.valueOf(3.0 / 100));
        return SalaryFlow.builder().ylaobxf(ylaobxf).yliaobxf(yliaobxf).sybxf(sybxf).zfgjj(zfgjj).grsds(grsds).build();
    }
}
