package com.interbanking.transaction.domain.port.out;

import com.interbanking.commons.models.entity.Empresa;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    Optional<Empresa> findById(Long id);
    Optional<Empresa> findByCuit(String cuit);
    List<Empresa> findAll();
    List<Empresa> findByFechaAdhesionAfter(LocalDateTime date);
    List<Empresa> findCompaniesWithTransfersInLastMonth();
    Empresa save(Empresa company);
    void deleteById(Long id);
    boolean existsByCuit(String cuit);
}