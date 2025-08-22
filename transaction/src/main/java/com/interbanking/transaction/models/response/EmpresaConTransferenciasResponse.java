package com.interbanking.transaction.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaConTransferenciasResponse {
    private Long id;
    private String cuit;
    private String razonSocial;
    private LocalDateTime fechaAdhesion;
    private Boolean activa;
    private List<TransferenciaData> transferencias;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferenciaData {
        private Long id;
        private BigDecimal importe;
        private Long empresaId;
        private String cuentaDebito;
        private String cuentaCredito;
        private LocalDateTime fechaTransferencia;
        private String estado;
    }
} 