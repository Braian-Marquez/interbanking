package com.interbanking.transaction.models.mapper;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.commons.models.entity.Transferencia;
import com.interbanking.transaction.models.request.EmpresaRequest;
import com.interbanking.transaction.models.response.EmpresaResponse;
import com.interbanking.transaction.models.response.EmpresaConTransferenciasResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmpresaMapper {
    
    public Empresa toEntity(EmpresaRequest request) {
        Empresa empresa = new Empresa();
        empresa.setCuit(request.getCuit());
        empresa.setRazonSocial(request.getRazonSocial());
        empresa.setFechaAdhesion(LocalDateTime.now());
        empresa.setActiva(true);
        return empresa;
    }
    
    public EmpresaResponse toResponse(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getCuit(),
                empresa.getRazonSocial(),
                empresa.getFechaAdhesion(),
                empresa.getActiva()
        );
    }
    
    public List<EmpresaResponse> toResponseList(List<Empresa> empresas) {
        return empresas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public EmpresaConTransferenciasResponse toResponseWithTransferencias(Empresa empresa) {
        List<EmpresaConTransferenciasResponse.TransferenciaData> transferenciasData = 
            empresa.getTransferencias().stream()
                .map(this::toTransferenciaData)
                .collect(Collectors.toList());
        
        return new EmpresaConTransferenciasResponse(
                empresa.getId(),
                empresa.getCuit(),
                empresa.getRazonSocial(),
                empresa.getFechaAdhesion(),
                empresa.getActiva(),
                transferenciasData
        );
    }
    
    private EmpresaConTransferenciasResponse.TransferenciaData toTransferenciaData(Transferencia transferencia) {
        return new EmpresaConTransferenciasResponse.TransferenciaData(
                transferencia.getId(),
                transferencia.getImporte(),
                transferencia.getEmpresa().getId(),
                transferencia.getCuentaDebito(),
                transferencia.getCuentaCredito(),
                transferencia.getFechaTransferencia(),
                transferencia.getEstado()
        );
    }
    
    public List<EmpresaConTransferenciasResponse> toResponseWithTransferenciasList(List<Empresa> empresas) {
        return empresas.stream()
                .map(this::toResponseWithTransferencias)
                .collect(Collectors.toList());
    }
} 