package com.interbanking.autentication.domain.port.out;

import com.interbanking.commons.models.entity.UserEntity;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAll();
    UserEntity save(UserEntity user);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}