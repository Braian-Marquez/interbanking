package com.interbanking.commons.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "importe", nullable = false)
    private BigDecimal importe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    
    @Column(name = "cuenta_debito", nullable = false)
    private String cuentaDebito;
    
    @Column(name = "cuenta_credito", nullable = false)
    private String cuentaCredito;
    
    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDateTime fechaTransferencia;
    
    @Column(name = "estado", nullable = false)
    private String estado = "COMPLETADA";
} 