package com.interbanking.autentication.infrastructure.adapter.out.persistence;

import com.interbanking.autentication.domain.port.out.UserRepository;
import com.interbanking.commons.models.entity.Role;
import com.interbanking.commons.models.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepository.findById(id)
            .map(this::toDomain);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
            .map(this::toDomain);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
            .map(this::toDomain);
    }

    @Override
    public List<UserEntity> findAll() {
        return userJpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public UserEntity save(UserEntity user) {
        UserEntity userEntity = toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    private UserEntity toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        String email = entity.getEmail();
        List<Role> roles = entity.getRoles().stream()
            .map(roleEntity -> new Role(roleEntity.getId(), roleEntity.getName(), roleEntity.getDescription()))
            .collect(Collectors.toList());

        UserEntity user = new UserEntity();
        user.setId(entity.getId());
        user.setEmail(email);
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRoles(roles);
     
        
        return user;
    }

    private UserEntity toEntity(UserEntity user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());


        if (user.getRoles() != null) {
            List<Role> roleEntities = user.getRoles().stream()
                .map(role -> {
                    Role roleEntity = new Role();
                    roleEntity.setId(role.getId());
                    roleEntity.setName(role.getName());
                    roleEntity.setDescription(role.getDescription());
                    return roleEntity;
                })
                .collect(Collectors.toList());
            entity.setRoles(roleEntities);
        }

        return entity;
    }
}