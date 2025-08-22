package com.interbanking.autentication.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.interbanking.autentication.models.repository.CustomerRepository;
import com.interbanking.autentication.models.repository.UserRepository;
import com.interbanking.autentication.models.request.UpdateUser;
import com.interbanking.autentication.models.response.CustomerResponse;
import com.interbanking.autentication.models.response.UserListResponse;
import com.interbanking.autentication.utils.Messenger;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.commons.models.entity.Customer;
import com.interbanking.commons.models.entity.UserEntity;
import com.interbanking.commons.models.enums.CodeEnum;
import com.interbanking.commons.models.enums.GenericPage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
 
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final Messenger messenger;

    public Page<UserListResponse> findAllWithFiltersPageable(GenericPage page) {
		int offset = page.getPageNumber() * page.getPageSize();
		long totalElements = userRepository.findAll().size();
		List<UserListResponse> response = userRepository.findUserPage(page.getSortDirection().toString(), page.getPageSize(), offset);
		return new PageImpl<>(response, PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSortDirection(), page.getSortBy()),
				totalElements);
	}

    public CustomerResponse getUserById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new NotFoundException("El usuario no existe.");
		}
        UserEntity user = userRepository.findById(customer.get().getIdUser()).orElseThrow(() -> new NotFoundException("El usuario no existe."));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setIdCustomer(customer.get().getId());
        customerResponse.setName(customer.get().getFirstName());
        customerResponse.setEmail(user.getEmail());
        customerResponse.setLastName(customer.get().getLastName());
        customerResponse.setIdUser(user.getId());
        return customerResponse;

    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isEmpty()) {
            throw new NotFoundException("El usuario no existe.");
        }
        Customer customer = customerOpt.get();
        Optional<UserEntity> userOpt = userRepository.findById(customer.getIdUser());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            int count = 1;
            String newEmail = user.getEmail() + "-deleted";
            while (userRepository.existsByEmail(newEmail)) {
                newEmail = user.getEmail() + "-deleted-" + count;
                count++;
            }
            user.setEmail(newEmail);
            user.setSoftDelete(true);  
            userRepository.save(user); 
        }
        customer.setSoftDelete(true);
        customerRepository.save(customer);
    }
    

    public CustomerResponse updateUser(Long id, UpdateUser userUpdateDTO) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new NotFoundException(messenger.getMessage(CodeEnum.CUSTOMER_NOT_EXIST));
        }
        customer.get().setFirstName(userUpdateDTO.getFirstName());
        customer.get().setLastName(userUpdateDTO.getLastName());
        customerRepository.save(customer.get());
        UserEntity user = userRepository.findById(customer.get().getIdUser())
                .orElseThrow(() -> new NotFoundException(messenger.getMessage(CodeEnum.CUSTOMER_NOT_EXIST)));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setIdCustomer(customer.get().getId());
        customerResponse.setName(customer.get().getFirstName());
        customerResponse.setEmail(user.getEmail());
        customerResponse.setLastName(customer.get().getLastName());
        customerResponse.setIdUser(user.getId());
        return customerResponse;
    }


    
}
