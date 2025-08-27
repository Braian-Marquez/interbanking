package com.interbanking.transaction.infrastructure.adapter.out.persistence;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.transaction.domain.port.out.CompanyRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class CompanyRepositoryAdapter implements CompanyRepository {
    private final CompanyJpaRepository companyJpaRepository;

    public CompanyRepositoryAdapter(CompanyJpaRepository companyJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Optional<Empresa> findById(Long id) {
        return companyJpaRepository.findById(id);
    }

    @Override
    public Optional<Empresa> findByCuit(String cuit) {
        return companyJpaRepository.findByCuit(cuit);
    }

    @Override
    public List<Empresa> findAll() {
        return companyJpaRepository.findAll();
    }

    @Override
    public List<Empresa> findByFechaAdhesionAfter(LocalDateTime date) {
        return companyJpaRepository.findByFechaAdhesionAfter(date);
    }

    @Override
    public List<Empresa> findCompaniesWithTransfersInLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return companyJpaRepository.findCompaniesWithTransfersInLastMonth(oneMonthAgo);
    }

    @Override
    public Empresa save(Empresa company) {
        return companyJpaRepository.save(company);
    }

    @Override
    public void deleteById(Long id) {
        companyJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCuit(String cuit) {
        return companyJpaRepository.existsByCuit(cuit);
    }
}