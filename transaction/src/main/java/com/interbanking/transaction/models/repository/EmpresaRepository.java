package com.interbanking.transaction.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.interbanking.commons.models.entity.Empresa;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    Optional<Empresa> findByCuit(String cuit);
    
    boolean existsByCuit(String cuit);
    
    @Query("SELECT DISTINCT t.empresa FROM Transferencia t " +
           "WHERE t.fechaTransferencia >= :fechaInicio " +
           "AND t.fechaTransferencia < :fechaFin " +
           "AND t.empresa.activa = true")
    List<Empresa> findEmpresasConTransferenciasEnPeriodo(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT e FROM Empresa e " +
           "WHERE e.fechaAdhesion >= :fechaInicio " +
           "AND e.fechaAdhesion < :fechaFin " +
           "AND e.activa = true")
    List<Empresa> findEmpresasAdheridasEnPeriodo(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT DISTINCT e FROM Empresa e " +
           "JOIN FETCH e.transferencias t " +
           "WHERE t.fechaTransferencia >= :fechaInicio " +
           "AND t.fechaTransferencia < :fechaFin " +
           "AND e.activa = true")
    List<Empresa> findEmpresasConTransferenciasEnPeriodoWithTransferencias(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
} 