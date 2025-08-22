package com.interbanking.transaction.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {
    
    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(regexp = "^[0-9]{2}-[0-9]{8}-[0-9]$", message = "El CUIT debe tener el formato XX-XXXXXXXX-X")
    private String cuit;
    
    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;
} 