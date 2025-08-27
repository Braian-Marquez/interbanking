package com.interbanking.autentication.domain.port.out;

import com.interbanking.commons.models.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Role save(Role role);
    void deleteById(Long id);
    boolean existsByName(String name);
}