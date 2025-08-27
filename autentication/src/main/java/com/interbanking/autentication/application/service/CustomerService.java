package com.interbanking.autentication.application.service;

import com.interbanking.autentication.domain.port.in.CustomerUseCase;
import com.interbanking.autentication.infrastructure.adapter.out.persistence.CustomerRepository;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.commons.models.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements CustomerUseCase {
    
    private final CustomerRepository customerRepository;
    
    @Override
    public Customer getById(Long id) {
        log.info("Getting customer by id: {}", id);
        return customerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
    }
    
    @Override
    public Page<Customer> getAllWithPagination(Pageable pageable, CustomerFilter filter) {
        log.info("Getting customers with pagination and filters: {}", filter);
        
        String firstName = (filter != null && StringUtils.hasText(filter.getFirstName())) ? filter.getFirstName() : null;
        String lastName = (filter != null && StringUtils.hasText(filter.getLastName())) ? filter.getLastName() : null;
        Long idUser = (filter != null) ? filter.getIdUser() : null;
        
        return customerRepository.findAllWithFilters(firstName, lastName, idUser, pageable);
    }
    
    @Override
    public Customer updateCustomer(UpdateCustomerCommand command) {
        log.info("Updating customer with id: {}", command.getId());
        
        Customer customer = customerRepository.findById(command.getId())
            .orElseThrow(() -> new NotFoundException("Customer not found with id: " + command.getId()));
        
        customer.setFirstName(command.getFirstName());
        customer.setLastName(command.getLastName());
        
        return customerRepository.save(customer);
    }
}
