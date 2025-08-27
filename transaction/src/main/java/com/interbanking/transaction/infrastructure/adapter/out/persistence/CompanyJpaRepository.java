package com.interbanking.transaction.infrastructure.adapter.out.persistence;

import com.interbanking.commons.models.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyJpaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCuit(String cuit);
    List<Empresa> findByFechaAdhesionAfter(LocalDateTime date);
    boolean existsByCuit(String cuit);
    
    @Query("SELECT DISTINCT e FROM Empresa e " +
           "JOIN e.transferencias t " +
           "WHERE t.fechaTransferencia > :oneMonthAgo")
    List<Empresa> findCompaniesWithTransfersInLastMonth(LocalDateTime oneMonthAgo);
}