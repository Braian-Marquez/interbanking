package com.interbanking.transaction.domain.model;

import java.math.BigDecimal;

import com.interbanking.commons.exception.NotFoundException;

public class Amount {
    private final BigDecimal value;

    public Amount(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotFoundException("Amount must be positive and not null");
        }
        this.value = value;
    }

    public Amount(String value) {
        this(new BigDecimal(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public Amount add(Amount other) {
        return new Amount(this.value.add(other.value));
    }

    public Amount subtract(Amount other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotFoundException("Result cannot be negative");
        }
        return new Amount(result);
    }

    public boolean isGreaterThan(Amount other) {
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(Amount other) {
        return this.value.compareTo(other.value) < 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Amount amount = (Amount) obj;
        return value.equals(amount.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}