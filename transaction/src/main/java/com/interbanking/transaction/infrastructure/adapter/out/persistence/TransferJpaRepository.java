package com.interbanking.transaction.infrastructure.adapter.out.persistence;

import com.interbanking.commons.models.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferJpaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByEmpresaId(Long empresaId);
    List<Transferencia> findByFechaTransferenciaAfter(LocalDateTime date);
    int countByEmpresaIdAndFechaTransferenciaAfter(Long empresaId, LocalDateTime date);
}