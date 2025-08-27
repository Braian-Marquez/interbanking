package com.interbanking.transaction.infrastructure.adapter.out.persistence;

import com.interbanking.transaction.domain.port.out.TransferValidationService;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TransferValidationServiceAdapter implements TransferValidationService {

    @Override
    public boolean validateAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        return accountNumber.matches("^\\d{10,20}$");
    }

    @Override
    public boolean validateAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        return amount.compareTo(BigDecimal.ZERO) > 0 && 
               amount.compareTo(new BigDecimal("1000000")) <= 0; 
    }


}