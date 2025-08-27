package com.interbanking.transaction.application.service;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.transaction.domain.port.in.GetRecentCompaniesUseCase;
import com.interbanking.transaction.domain.port.out.CompanyRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentCompanyService implements GetRecentCompaniesUseCase {
    
    private final CompanyRepository companyRepository;


    @Override
    public List<Empresa> execute() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return companyRepository.findByFechaAdhesionAfter(oneMonthAgo);
    }
}