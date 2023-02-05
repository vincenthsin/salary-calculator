package com.idiosoul.salarycalculator.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class SocialInsuranceExpense {
    private BigDecimal ylaobxf;
    private BigDecimal yliaobxf;
    private BigDecimal sybxf;
    private BigDecimal zfgjj;
    private BigDecimal siTotalExpense;

}
