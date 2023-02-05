package com.idiosoul.salarycalculator.api;

import com.idiosoul.salarycalculator.model.PayrollSlip;
import com.idiosoul.salarycalculator.model.SalaryFlow;
import com.idiosoul.salarycalculator.model.SocialInsuranceExpense;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

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
        return null;
    }

    @PostMapping("/salarybymonth")
    public Map<Integer, SalaryFlow> calculateByMonth(@RequestBody TreeMap<Integer, PayrollSlip> payrollSlips) {
        Map<Integer, SalaryFlow> salaryFlows = new TreeMap();
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        BigDecimal totalWithholdingTaxAmount = BigDecimal.ZERO;
        BigDecimal totalYnse = BigDecimal.ZERO;
        BigDecimal totalSalary = BigDecimal.ZERO;
        for (Integer month : payrollSlips.keySet()) {
            PayrollSlip payrollSlip = payrollSlips.get(month);
            BigDecimal grossBaseSalary = payrollSlip.getGrossBaseSalary();

            // Calculate social insurance expense
            SocialInsuranceExpense socialInsuranceExpense = calculateSocialInsurance(grossBaseSalary, payrollSlip.getSiBaseAmount());

            // Calculate tax expense
            totalSalary = totalSalary.add(grossBaseSalary); // Just for debug
            BigDecimal taxableAmount = grossBaseSalary.subtract(socialInsuranceExpense.getSiTotalExpense()).subtract(BigDecimal.valueOf(5000));
            totalTaxableAmount = totalTaxableAmount.add(taxableAmount);
            BigDecimal withholdingTaxAmount = calculateTaxExpense(totalTaxableAmount).subtract(totalWithholdingTaxAmount);

            // Calculate discretionaryAmount
            BigDecimal discretionaryAmount = grossBaseSalary.subtract(socialInsuranceExpense.getSiTotalExpense()).subtract(withholdingTaxAmount);

            SalaryFlow salaryFlow = SalaryFlow.builder().socialInsuranceExpense(socialInsuranceExpense).taxAmount(withholdingTaxAmount).discretionaryAmount(discretionaryAmount).build();
            salaryFlows.put(month, salaryFlow);
            totalWithholdingTaxAmount = totalWithholdingTaxAmount.add(withholdingTaxAmount);
        }
        return salaryFlows;
    }

    private SocialInsuranceExpense calculateSocialInsurance(BigDecimal grossBaseSalary, BigDecimal siBaseSalary) {
        BigDecimal ylaobxf = siBaseSalary.multiply(BigDecimal.valueOf(8.0 / 100)).setScale(1, RoundingMode.UP);
        BigDecimal yliaobxf = siBaseSalary.multiply(BigDecimal.valueOf(2.0 / 100)).setScale(1, RoundingMode.UP);
        BigDecimal sybxf = siBaseSalary.multiply(BigDecimal.valueOf(0.5 / 100)).setScale(1, RoundingMode.UP);

        BigDecimal zfgjj = siBaseSalary.multiply(BigDecimal.valueOf(7.0 / 100)).setScale(0, RoundingMode.HALF_UP);

        BigDecimal siTotalExpense = ylaobxf.add(yliaobxf).add(sybxf).add(zfgjj);

        return SocialInsuranceExpense.builder().ylaobxf(ylaobxf).yliaobxf(yliaobxf).sybxf(sybxf).zfgjj(zfgjj).siTotalExpense(siTotalExpense).build();
    }

    private BigDecimal calculateTaxExpense(BigDecimal totalTaxableAmount) {
        if (totalTaxableAmount.compareTo(BigDecimal.ZERO) <= 0)
            return BigDecimal.ZERO;
        BigDecimal taxAmount;
        if (totalTaxableAmount.compareTo(BigDecimal.valueOf(36000)) <= 0) {
            taxAmount = totalTaxableAmount.multiply(BigDecimal.valueOf(3.0 / 100));
        } else if (totalTaxableAmount.compareTo(BigDecimal.valueOf(36000)) > 0 && totalTaxableAmount.compareTo(BigDecimal.valueOf(144000)) <= 0) {
            taxAmount = totalTaxableAmount.multiply(BigDecimal.valueOf(10.0 / 100)).subtract(BigDecimal.valueOf(2520));
        } else {
            taxAmount = totalTaxableAmount.multiply(BigDecimal.valueOf(20.0 / 100)).subtract(BigDecimal.valueOf(16920));
        }
        return taxAmount.setScale(2,RoundingMode.HALF_UP);
    }
}
