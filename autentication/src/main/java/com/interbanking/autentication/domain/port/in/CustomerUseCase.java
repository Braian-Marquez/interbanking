package com.interbanking.autentication.domain.port.in;

import com.interbanking.commons.models.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerUseCase {
    
    interface UpdateCustomerCommand {
        Long getId();
        String getFirstName();
        String getLastName();
    }
    
    interface CustomerFilter {
        String getFirstName();
        String getLastName();
        Long getIdUser();
    }
    Customer getById(Long id);
    Page<Customer> getAllWithPagination(Pageable pageable, CustomerFilter filter);
    Customer updateCustomer(UpdateCustomerCommand command);
}
