package com.interbanking.transaction.infrastructure.adapter.in.web;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.transaction.domain.port.in.GetCompaniesWithTransfersUseCase;
import com.interbanking.transaction.domain.port.in.GetRecentCompaniesUseCase;
import com.interbanking.transaction.domain.port.in.RegisterCompanyUseCase;
import com.interbanking.transaction.infrastructure.adapter.in.web.request.CompanyRequest;
import com.interbanking.transaction.infrastructure.adapter.in.web.response.CompanyResponse;
import com.interbanking.transaction.infrastructure.adapter.in.web.response.CompanyWithTransfersResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/v1/empresas")
@RequiredArgsConstructor
@Validated
public class CompanyController {

    private final RegisterCompanyUseCase registerCompanyUseCase;
    private final GetCompaniesWithTransfersUseCase getCompaniesWithTransfersUseCase;
    private final GetRecentCompaniesUseCase getRecentCompaniesUseCase;

    @GetMapping("/transferencias-ultimo-mes")
    public ResponseEntity<List<CompanyWithTransfersResponse>> getCompaniesWithTransfersLastMonth() {
        List<GetCompaniesWithTransfersUseCase.CompanyWithTransfersResult> results = getCompaniesWithTransfersUseCase
                .execute();

        List<CompanyWithTransfersResponse> response = results.stream()
                .map(result -> CompanyWithTransfersResponse.from(result.getCompany(), result.getTransferCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/adheridas-ultimo-mes")
    public ResponseEntity<List<CompanyResponse>> getRecentRegisteredCompanies() {
        List<Empresa> companies = getRecentCompaniesUseCase.execute();
        List<CompanyResponse> response = companies.stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/adherir")
    public ResponseEntity<CompanyResponse> registerCompany(@Valid @RequestBody CompanyRequest request) {
        RegisterCompanyUseCase.RegisterCompanyCommand command = new RegisterCompanyUseCase.RegisterCompanyCommand(
                request.getCuit(), request.getCompanyName());

        Empresa company = registerCompanyUseCase.execute(command);
        CompanyResponse response = CompanyResponse.from(company);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}