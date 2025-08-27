package com.interbanking.transaction.infrastructure.adapter.in.web.response;

import com.interbanking.commons.models.entity.Empresa;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWithTransfersResponse {
    private Long id;
    private String cuit;
    private String companyName;
    private LocalDateTime registrationDate;
    private boolean active;
    private int transferCount;

 

    public static CompanyWithTransfersResponse from(Empresa empresa, int transferCount) {
        return new CompanyWithTransfersResponse(
            empresa.getId(),
            empresa.getCuit(),
            empresa.getRazonSocial(),
            empresa.getFechaAdhesion(),
            empresa.getActiva(),
            transferCount
        );
    }

}