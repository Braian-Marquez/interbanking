package com.interbanking.autentication.infrastructure.adapter.in.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.interbanking.autentication.domain.port.in.CustomerUseCase;
import com.interbanking.autentication.infrastructure.adapter.in.web.request.UpdateCustomerRequest;
import com.interbanking.autentication.infrastructure.adapter.in.web.response.CustomerPageResponse;
import com.interbanking.autentication.infrastructure.adapter.in.web.response.CustomerResponse;
import com.interbanking.commons.models.entity.Customer;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/user/v1")
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    @GetMapping("/customers")
    public ResponseEntity<CustomerPageResponse> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long idUser) {
        System.out.println("llegando a getAllCustomers");
        Pageable pageable = PageRequest.of(page, size);
        
        CustomerUseCase.CustomerFilter filter = new CustomerUseCase.CustomerFilter() {
            @Override
            public String getFirstName() { return firstName; }
            @Override
            public String getLastName() { return lastName; }
            @Override
            public Long getIdUser() { return idUser; }
        };
        
        Page<Customer> customerPage = customerUseCase.getAllWithPagination(pageable, filter);
        CustomerPageResponse response = CustomerPageResponse.from(customerPage);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Customer customer = customerUseCase.getById(id);
        CustomerResponse response = CustomerResponse.from(customer);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-customer")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @RequestParam(required = true) Long id,
            @Validated @RequestBody UpdateCustomerRequest request) {
        
        CustomerUseCase.UpdateCustomerCommand command = new CustomerUseCase.UpdateCustomerCommand() {
            @Override
            public Long getId() { return id; }
            @Override
            public String getFirstName() { return request.getFirstName(); }
            @Override
            public String getLastName() { return request.getLastName(); }
        };
        Customer updatedCustomer = customerUseCase.updateCustomer(command);
        CustomerResponse response = CustomerResponse.from(updatedCustomer);
        return ResponseEntity.ok(response);
    }

}
