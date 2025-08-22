package com.interbanking.transaction.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {
    private Long id;
    private String cuit;
    private String razonSocial;
    private LocalDateTime fechaAdhesion;
    private Boolean activa;
} 