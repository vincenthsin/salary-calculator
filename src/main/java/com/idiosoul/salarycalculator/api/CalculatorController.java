package com.idiosoul.salarycalculator.api;

import com.idiosoul.salarycalculator.model.SalaryFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CalculatorController {
    @GetMapping("/salary")
    public SalaryFlow calculate(BigDecimal grossBaseSalary) {
        BigDecimal siBaseSalary = BigDecimal.valueOf(14000);
        BigDecimal siExcludeSalary = grossBaseSalary.subtract(BigDecimal.valueOf(14000));
        BigDecimal ylaobxf = siBaseSalary.multiply(BigDecimal.valueOf(8.0 / 100));
        BigDecimal yliaobxf = siBaseSalary.multiply(BigDecimal.valueOf(2.0 / 100));
        BigDecimal sybxf = siBaseSalary.multiply(BigDecimal.valueOf(0.5 / 100));
        BigDecimal zfgjj = siBaseSalary.multiply(BigDecimal.valueOf(7.0 / 100));

        BigDecimal pendingTaxAmount = siBaseSalary.multiply(BigDecimal.valueOf(1 - 17.5 / 100)).add(siExcludeSalary);
        BigDecimal grsds = pendingTaxAmount.subtract(BigDecimal.valueOf(5000)).multiply(BigDecimal.valueOf(3.0 / 100));
        BigDecimal taxedAmount = pendingTaxAmount.subtract(grsds);
        return SalaryFlow.builder().ylaobxf(ylaobxf).yliaobxf(yliaobxf).sybxf(sybxf).zfgjj(zfgjj).grsds(grsds).discretionaryAmount(taxedAmount).build();
    }

    @GetMapping("/salarybymonth")
    public SalaryFlow calculateByMonth(BigDecimal grossBaseSalary, int month, BigDecimal siBaseSalary) {
        BigDecimal siExcludeSalary = grossBaseSalary.subtract(siBaseSalary);
        BigDecimal ylaobxf = siBaseSalary.multiply(BigDecimal.valueOf(8.0 / 100));
        BigDecimal yliaobxf = siBaseSalary.multiply(BigDecimal.valueOf(2.0 / 100));
        BigDecimal sybxf = siBaseSalary.multiply(BigDecimal.valueOf(0.5 / 100));
        BigDecimal zfgjj = siBaseSalary.multiply(BigDecimal.valueOf(7.0 / 100));

        BigDecimal pendingTaxAmount = siBaseSalary.multiply(BigDecimal.valueOf(1 - 17.5 / 100)).add(siExcludeSalary);
        BigDecimal cumulative = pendingTaxAmount.subtract(BigDecimal.valueOf(5000)).multiply(BigDecimal.valueOf(month));
        BigDecimal grsds;
        if (cumulative.compareTo(BigDecimal.valueOf(36000)) <= 0) {
            grsds = pendingTaxAmount.subtract(BigDecimal.valueOf(5000)).multiply(BigDecimal.valueOf(3.0 / 100));
        } else if (cumulative.compareTo(BigDecimal.valueOf(36000)) > 0 && cumulative.compareTo(BigDecimal.valueOf(144000)) <= 0) {
            grsds = pendingTaxAmount.subtract(BigDecimal.valueOf(5000 + 2520)).multiply(BigDecimal.valueOf(3.0 / 100));
        } else {
            grsds = pendingTaxAmount.subtract(BigDecimal.valueOf(5000 + 16920)).multiply(BigDecimal.valueOf(3.0 / 100));
        }
        BigDecimal taxedAmount = pendingTaxAmount.subtract(grsds);
        return SalaryFlow.builder().ylaobxf(ylaobxf).yliaobxf(yliaobxf).sybxf(sybxf).zfgjj(zfgjj).grsds(grsds).discretionaryAmount(taxedAmount).build();
    }
}
