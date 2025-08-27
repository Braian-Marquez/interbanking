package com.interbanking.transaction.application.service;

import com.interbanking.commons.models.entity.Empresa;
import com.interbanking.commons.models.entity.Transferencia;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.transaction.domain.port.in.CreateTransferUseCase.*;
import com.interbanking.transaction.domain.port.in.GetCompaniesWithTransfersUseCase.*;
import com.interbanking.transaction.domain.port.in.RegisterCompanyUseCase.*;
import com.interbanking.transaction.domain.port.out.CompanyRepository;
import com.interbanking.transaction.domain.port.out.TransferRepository;
import com.interbanking.transaction.domain.port.out.TransferValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private TransferRepository transferRepository;
    
    @Mock
    private TransferValidationService transferValidationService;

    @InjectMocks
    private TransactionService transactionService;

    private Empresa testCompany;
    private Transferencia testTransfer;
    private RegisterCompanyCommand registerCompanyCommand;
    private CreateTransferCommand createTransferCommand;

    @BeforeEach
    void setUp() {
        testCompany = new Empresa();
        testCompany.setId(1L);
        testCompany.setCuit("20-12345678-9");
        testCompany.setRazonSocial("Test Company S.A.");
        testCompany.setFechaAdhesion(LocalDateTime.now());
        testCompany.setActiva(true);

        testTransfer = new Transferencia();
        testTransfer.setId(1L);
        testTransfer.setImporte(new BigDecimal("1000.00"));
        testTransfer.setEmpresa(testCompany);
        testTransfer.setCuentaDebito("1234567890");
        testTransfer.setCuentaCredito("0987654321");
        testTransfer.setFechaTransferencia(LocalDateTime.now());
        testTransfer.setEstado("COMPLETADA");

        registerCompanyCommand = RegisterCompanyCommand.builder()
            .cuit("20-12345678-9")
            .companyName("Test Company S.A.")
            .build();

        createTransferCommand = CreateTransferCommand.builder()
            .companyId(1L)
            .amount(new BigDecimal("1000.00"))
            .debitAccount("1234567890")
            .creditAccount("0987654321")
            .build();
    }

    @Test
    void execute_RegisterCompany_Success() {
        when(companyRepository.existsByCuit(anyString())).thenReturn(false);
        when(companyRepository.save(any(Empresa.class))).thenReturn(testCompany);

        Empresa result = transactionService.execute(registerCompanyCommand);

        assertNotNull(result);
        assertEquals("20-12345678-9", result.getCuit());
        assertEquals("Test Company S.A.", result.getRazonSocial());
        assertTrue(result.getActiva());
        assertNotNull(result.getFechaAdhesion());
        
        verify(companyRepository).save(any(Empresa.class));
    }

    @Test
    void execute_RegisterCompany_CompanyAlreadyExists_ThrowsException() {
        when(companyRepository.existsByCuit(anyString())).thenReturn(true);

        assertThrows(NotFoundException.class, 
            () -> transactionService.execute(registerCompanyCommand));
        
        verify(companyRepository, never()).save(any(Empresa.class));
    }

    @Test
    void execute_GetCompaniesWithTransfers_Success() {
        List<Empresa> companies = List.of(testCompany);
        when(companyRepository.findCompaniesWithTransfersInLastMonth()).thenReturn(companies);
        when(transferRepository.countByEmpresaIdAndFechaTransferenciaAfter(anyLong(), any(LocalDateTime.class)))
            .thenReturn(5);

        List<CompanyWithTransfersResult> results = transactionService.execute();

        assertNotNull(results);
        assertEquals(1, results.size());
        
        CompanyWithTransfersResult result = results.get(0);
        assertEquals(testCompany, result.getCompany());
        assertEquals(5, result.getTransferCount());
    }

    @Test
    void execute_GetCompaniesWithTransfers_NoCompanies_ReturnsEmptyList() {
        when(companyRepository.findCompaniesWithTransfersInLastMonth()).thenReturn(List.of());

        List<CompanyWithTransfersResult> results = transactionService.execute();

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getRecentCompanies_Success() {
        List<Empresa> companies = List.of(testCompany);
        when(companyRepository.findByFechaAdhesionAfter(any(LocalDateTime.class))).thenReturn(companies);

        List<Empresa> results = transactionService.getRecentCompanies();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testCompany, results.get(0));
        
        verify(companyRepository).findByFechaAdhesionAfter(any(LocalDateTime.class));
    }

    @Test
    void execute_CreateTransfer_Success() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(transferValidationService.validateAmount(any(BigDecimal.class))).thenReturn(true);
        when(transferValidationService.validateAccount("1234567890")).thenReturn(true);
        when(transferValidationService.validateAccount("0987654321")).thenReturn(true);
        when(transferRepository.save(any(Transferencia.class))).thenReturn(testTransfer);

        Transferencia result = transactionService.execute(createTransferCommand);

        assertNotNull(result);
        assertEquals(new BigDecimal("1000.00"), result.getImporte());
        assertEquals(testCompany, result.getEmpresa());
        assertEquals("1234567890", result.getCuentaDebito());
        assertEquals("0987654321", result.getCuentaCredito());
        assertEquals("COMPLETADA", result.getEstado());
        assertNotNull(result.getFechaTransferencia());
        
        verify(transferRepository).save(any(Transferencia.class));
    }

    @Test
    void execute_CreateTransfer_CompanyNotFound_ThrowsException() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, 
            () -> transactionService.execute(createTransferCommand));
        
        verify(transferRepository, never()).save(any(Transferencia.class));
    }

    @Test
    void execute_CreateTransfer_InvalidAmount_ThrowsException() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(transferValidationService.validateAmount(any(BigDecimal.class))).thenReturn(false);

        assertThrows(IllegalArgumentException.class, 
            () -> transactionService.execute(createTransferCommand));
        
        verify(transferRepository, never()).save(any(Transferencia.class));
    }

   

    @Test
    void execute_CreateTransfer_InvalidCreditAccount_ThrowsException() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(transferValidationService.validateAmount(any(BigDecimal.class))).thenReturn(true);
        when(transferValidationService.validateAccount("1234567890")).thenReturn(true);
        when(transferValidationService.validateAccount("0987654321")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, 
            () -> transactionService.execute(createTransferCommand));
        
        verify(transferRepository, never()).save(any(Transferencia.class));
    }

   
}