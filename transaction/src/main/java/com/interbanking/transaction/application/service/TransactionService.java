package com.interbanking.transaction.application.service;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.commons.models.entity.Transferencia;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.transaction.domain.port.in.CreateTransferUseCase;
import com.interbanking.transaction.domain.port.in.GetCompaniesWithTransfersUseCase;
import com.interbanking.transaction.domain.port.in.RegisterCompanyUseCase;
import com.interbanking.transaction.domain.port.out.CompanyRepository;
import com.interbanking.transaction.domain.port.out.TransferRepository;
import com.interbanking.transaction.domain.port.out.TransferValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements RegisterCompanyUseCase, GetCompaniesWithTransfersUseCase, 
                                          CreateTransferUseCase {
    
    private final CompanyRepository companyRepository;
    private final TransferRepository transferRepository;
    private final TransferValidationService transferValidationService;


    @Override
    public Empresa execute(RegisterCompanyCommand command) {
        if (companyRepository.existsByCuit(command.getCuit())) {
            throw new NotFoundException("Company with CUIT " + command.getCuit() + " already exists");
        }

        Empresa empresa = new Empresa();
        empresa.setCuit(command.getCuit());
        empresa.setRazonSocial(command.getCompanyName());
        empresa.setFechaAdhesion(LocalDateTime.now());
        empresa.setActiva(true);

        return companyRepository.save(empresa);
    }

    @Override
    public List<CompanyWithTransfersResult> execute() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Empresa> companies = companyRepository.findCompaniesWithTransfersInLastMonth();
        
        return companies.stream()
            .map(company -> {
                int transferCount = transferRepository.countByEmpresaIdAndFechaTransferenciaAfter(
                    company.getId(), oneMonthAgo);
                return new CompanyWithTransfersResult(company, transferCount);
            })
            .collect(Collectors.toList());
    }

    public List<Empresa> getRecentCompanies() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return companyRepository.findByFechaAdhesionAfter(oneMonthAgo);
    }

    @Override
    public Transferencia execute(CreateTransferCommand command) {

        Empresa company = companyRepository.findById(command.getCompanyId())
            .orElseThrow(() -> new NotFoundException("Company not found with id: " + command.getCompanyId()));

        if (!transferValidationService.validateAmount(command.getAmount())) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }

        if (!transferValidationService.validateAccount(command.getDebitAccount()) ||
            !transferValidationService.validateAccount(command.getCreditAccount())) {
            throw new IllegalArgumentException("Invalid account number");
        }

    
        Transferencia transferencia = new Transferencia();
        transferencia.setImporte(command.getAmount());
        transferencia.setEmpresa(company);
        transferencia.setCuentaDebito(command.getDebitAccount());
        transferencia.setCuentaCredito(command.getCreditAccount());
        transferencia.setFechaTransferencia(LocalDateTime.now());
        transferencia.setEstado("COMPLETADA");

        return transferRepository.save(transferencia);
    }
}