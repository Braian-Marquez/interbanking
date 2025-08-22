package com.interbanking.transaction.controller;

import com.interbanking.transaction.models.request.EmpresaRequest;
import com.interbanking.transaction.models.response.EmpresaResponse;
import com.interbanking.transaction.models.response.EmpresaConTransferenciasResponse;
import com.interbanking.transaction.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/v1/empresas")
@RequiredArgsConstructor
public class TransactionController {
    
    private final EmpresaService empresaService;
 
    @GetMapping("/transferencias-ultimo-mes")
    public ResponseEntity<List<EmpresaConTransferenciasResponse>> getEmpresasConTransferenciasUltimoMes() {
        List<EmpresaConTransferenciasResponse> empresas = empresaService.getEmpresasConTransferenciasUltimoMes();
        return ResponseEntity.ok(empresas);
    }
    
    @GetMapping("/adheridas-ultimo-mes")
    public ResponseEntity<List<EmpresaResponse>> getEmpresasAdheridasUltimoMes() {
        List<EmpresaResponse> empresas = empresaService.getEmpresasAdheridasUltimoMes();
        return ResponseEntity.ok(empresas);
    }
    
    @PostMapping("/adherir")
    public ResponseEntity<EmpresaResponse> adherirEmpresa(@Valid @RequestBody EmpresaRequest request) {
        EmpresaResponse empresa = empresaService.adherirEmpresa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }
}
