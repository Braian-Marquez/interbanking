package com.interbanking.transaction.service;

import com.interbanking.transaction.models.request.EmpresaRequest;
import com.interbanking.transaction.models.response.EmpresaResponse;
import com.interbanking.transaction.models.response.EmpresaConTransferenciasResponse;
import java.util.List;

public interface EmpresaService {

    List<EmpresaConTransferenciasResponse> getEmpresasConTransferenciasUltimoMes();
    
    List<EmpresaResponse> getEmpresasAdheridasUltimoMes();

    EmpresaResponse adherirEmpresa(EmpresaRequest request);
} 