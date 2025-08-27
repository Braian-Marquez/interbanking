package com.interbanking.autentication.infrastructure.adapter.out.persistence;

import com.interbanking.commons.models.entity.Role;
import com.interbanking.autentication.domain.port.out.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}

@Component
public class RoleRepositoryAdapter implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id)
            .map(this::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name)
            .map(this::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Role save(Role role) {
        Role roleEntity = toEntity(role);
        Role savedEntity = roleJpaRepository.save(roleEntity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        roleJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    private Role toDomain(Role entity) {
        if (entity == null) {
            return null;
        }
        return new Role(entity.getId(), entity.getName(), entity.getDescription());
    }

    private Role toEntity(Role role) {
        if (role == null) {
            return null;
        }
        return new Role(role.getId(), role.getName(), role.getDescription());
    }
}