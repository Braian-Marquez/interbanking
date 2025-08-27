package com.interbanking.transaction.infrastructure.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {
    
    @NotBlank(message = "CUIT is required")
    @Pattern(regexp = "^\\d{11}$", message = "CUIT must have 11 digits")
    private String cuit;
    
    @NotBlank(message = "Company name is required")
    private String companyName;

}