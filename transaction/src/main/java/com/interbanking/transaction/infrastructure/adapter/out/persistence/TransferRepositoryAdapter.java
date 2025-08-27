package com.interbanking.transaction.infrastructure.adapter.out.persistence;

import com.interbanking.commons.models.entity.Transferencia;
import com.interbanking.transaction.domain.port.out.TransferRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TransferRepositoryAdapter implements TransferRepository {
    private final TransferJpaRepository transferJpaRepository;

    public TransferRepositoryAdapter(TransferJpaRepository transferJpaRepository) {
        this.transferJpaRepository = transferJpaRepository;
    }

    @Override
    public Optional<Transferencia> findById(Long id) {
        return transferJpaRepository.findById(id);
    }

    @Override
    public List<Transferencia> findAll() {
        return transferJpaRepository.findAll();
    }

    @Override
    public List<Transferencia> findByEmpresaId(Long empresaId) {
        return transferJpaRepository.findByEmpresaId(empresaId);
    }

    @Override
    public List<Transferencia> findByFechaTransferenciaAfter(LocalDateTime date) {
        return transferJpaRepository.findByFechaTransferenciaAfter(date);
    }

    @Override
    public Transferencia save(Transferencia transfer) {
        return transferJpaRepository.save(transfer);
    }

    @Override
    public void deleteById(Long id) {
        transferJpaRepository.deleteById(id);
    }

    @Override
    public int countByEmpresaIdAndFechaTransferenciaAfter(Long empresaId, LocalDateTime date) {
        return transferJpaRepository.countByEmpresaIdAndFechaTransferenciaAfter(empresaId, date);
    }
}