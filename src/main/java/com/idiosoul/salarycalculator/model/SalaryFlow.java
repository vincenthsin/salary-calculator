package com.idiosoul.salarycalculator.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class SalaryFlow {
    SocialInsuranceExpense socialInsuranceExpense;
    BigDecimal taxAmount;
    private BigDecimal discretionaryAmount;
}
