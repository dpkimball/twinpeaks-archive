package com.miles.twinpeaks.domain.usda.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Measure {

    private String label;

    private BigDecimal eqv;

    private String eunit;

    private String qty;

    private BigDecimal value;

    public Measure(){}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getEqv() {
        return eqv;
    }

    public void setEqv(BigDecimal eqv) {
        this.eqv = eqv;
    }

    public String getEunit() {
        return eunit;
    }

    public void setEunit(String eunit) {
        this.eunit = eunit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
