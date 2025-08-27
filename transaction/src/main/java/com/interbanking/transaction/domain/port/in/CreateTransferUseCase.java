package com.interbanking.transaction.domain.port.in;

import com.interbanking.commons.models.entity.Transferencia;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public interface CreateTransferUseCase {
    Transferencia execute(CreateTransferCommand command);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTransferCommand {
        private BigDecimal amount;
        private Long companyId;
        private String debitAccount;
        private String creditAccount;

    }
}