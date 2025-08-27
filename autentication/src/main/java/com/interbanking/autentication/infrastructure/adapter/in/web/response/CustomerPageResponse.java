package com.interbanking.autentication.infrastructure.adapter.in.web.response;

import com.interbanking.commons.models.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPageResponse {
    private List<CustomerResponse> customers;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private boolean hasNext;
    private boolean hasPrevious;

    public static CustomerPageResponse from(Page<Customer> customerPage) {
        List<CustomerResponse> customerResponses = customerPage.getContent().stream()
            .map(CustomerResponse::from)
            .toList();

        return CustomerPageResponse.builder()
            .customers(customerResponses)
            .currentPage(customerPage.getNumber())
            .totalPages(customerPage.getTotalPages())
            .totalElements(customerPage.getTotalElements())
            .size(customerPage.getSize())
            .hasNext(customerPage.hasNext())
            .hasPrevious(customerPage.hasPrevious())
            .build();
    }
}
