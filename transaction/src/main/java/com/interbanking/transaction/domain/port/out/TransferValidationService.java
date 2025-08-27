package com.interbanking.transaction.domain.port.out;

import java.math.BigDecimal;

public interface TransferValidationService {
    boolean validateAccount(String accountNumber);
    boolean validateAmount(BigDecimal amount);
}