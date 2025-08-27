package com.interbanking.autentication.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.interbanking.commons.models.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Query(value = "SELECT * FROM customer c WHERE " +
           "(:firstName IS NULL OR LOWER(c.first_name::text) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(c.last_name::text) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:idUser IS NULL OR c.id_user = :idUser) AND c.soft_delete = false",
       countQuery = "SELECT COUNT(*) FROM customer c WHERE " +
           "(:firstName IS NULL OR LOWER(c.first_name::text) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(c.last_name::text) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:idUser IS NULL OR c.id_user = :idUser) AND c.soft_delete = false",
       nativeQuery = true)
    Page<Customer> findAllWithFilters(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("idUser") Long idUser,
        Pageable pageable
    );
}
