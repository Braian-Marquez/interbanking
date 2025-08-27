package com.interbanking.transaction.domain.model;

import com.interbanking.commons.exception.InvalidCredentialsException;

public class AccountNumber {
    private final String value;

    public AccountNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidCredentialsException("Account number cannot be null or empty");
        }
        
        if (!isValidAccountNumber(value.trim())) {
            throw new InvalidCredentialsException("Invalid account number format: " + value);
        }
        
        this.value = value.trim();
    }

    private boolean isValidAccountNumber(String accountNumber) {
        return accountNumber.matches("^\\d{10,20}$");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountNumber that = (AccountNumber) obj;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}