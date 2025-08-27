package com.interbanking.transaction.domain.port.out;

import com.interbanking.commons.models.entity.Transferencia;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransferRepository {
    Optional<Transferencia> findById(Long id);
    List<Transferencia> findAll();
    List<Transferencia> findByEmpresaId(Long empresaId);
    List<Transferencia> findByFechaTransferenciaAfter(LocalDateTime date);
    Transferencia save(Transferencia transfer);
    void deleteById(Long id);
    int countByEmpresaIdAndFechaTransferenciaAfter(Long empresaId, LocalDateTime date);
}