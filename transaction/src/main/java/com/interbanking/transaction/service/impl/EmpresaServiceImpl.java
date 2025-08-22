package com.interbanking.transaction.service.impl;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.transaction.models.mapper.EmpresaMapper;
import com.interbanking.transaction.models.repository.EmpresaRepository;
import com.interbanking.transaction.models.request.EmpresaRequest;
import com.interbanking.transaction.models.response.EmpresaResponse;
import com.interbanking.transaction.models.response.EmpresaConTransferenciasResponse;
import com.interbanking.transaction.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpresaServiceImpl implements EmpresaService {
    
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<EmpresaConTransferenciasResponse> getEmpresasConTransferenciasUltimoMes() {
        LocalDateTime fechaInicio = LocalDateTime.now().minusMonths(1);
        LocalDateTime fechaFin = LocalDateTime.now();
        List<Empresa> empresas = empresaRepository.findEmpresasConTransferenciasEnPeriodoWithTransferencias(fechaInicio, fechaFin);
        return empresaMapper.toResponseWithTransferenciasList(empresas);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> getEmpresasAdheridasUltimoMes() {
        LocalDateTime fechaInicio = LocalDateTime.now().minusMonths(1);
        LocalDateTime fechaFin = LocalDateTime.now();
        
        List<Empresa> empresas = empresaRepository.findEmpresasAdheridasEnPeriodo(fechaInicio, fechaFin);
        return empresaMapper.toResponseList(empresas);
    }
    
    @Override
    public EmpresaResponse adherirEmpresa(EmpresaRequest request) {
        if (empresaRepository.existsByCuit(request.getCuit())) {
            throw new RuntimeException("Ya existe una empresa con el CUIT: " + request.getCuit());
        }
        Empresa empresa = empresaMapper.toEntity(request);
        Empresa empresaGuardada = empresaRepository.save(empresa);
        return empresaMapper.toResponse(empresaGuardada);
    }
} 