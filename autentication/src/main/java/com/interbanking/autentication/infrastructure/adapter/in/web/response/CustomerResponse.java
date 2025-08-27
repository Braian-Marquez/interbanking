package com.interbanking.autentication.infrastructure.adapter.in.web.response;

import com.interbanking.commons.models.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Long idUser;

    public static CustomerResponse from(Customer customer) {
        return CustomerResponse.builder()
            .id(customer.getId())
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .idUser(customer.getIdUser())
            .build();
    }
}
