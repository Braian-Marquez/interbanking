package com.interbanking.transaction.domain.port.in;

import com.interbanking.commons.models.entity.Empresa;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
    
public interface RegisterCompanyUseCase {
    Empresa execute(RegisterCompanyCommand command);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RegisterCompanyCommand {
        private String cuit;
        private String companyName;

    }
}