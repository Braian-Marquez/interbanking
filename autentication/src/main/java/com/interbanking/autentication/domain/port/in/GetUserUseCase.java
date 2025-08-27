package com.interbanking.autentication.domain.port.in;

import com.interbanking.commons.models.entity.UserEntity;
import java.util.List;

public interface GetUserUseCase {
    UserEntity getById(Long id);
    UserEntity getByUsername(String username);
    UserEntity getByEmail(String email);
    List<UserEntity> getAll();
}