package com.interbanking.commons.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "cuit", unique = true, nullable = false)
    private String cuit;
    
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;
    
    @Column(name = "fecha_adhesion", nullable = false)
    private LocalDateTime fechaAdhesion;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transferencia> transferencias;
} 