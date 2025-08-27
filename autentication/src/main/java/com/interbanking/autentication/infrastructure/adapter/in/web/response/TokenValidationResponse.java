package com.interbanking.autentication.infrastructure.adapter.in.web.response;

import com.interbanking.commons.models.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;

    public static TokenValidationResponse from(UserEntity user) {
        List<String> roleNames = user.getRoles().stream()
            .map(role -> role.getName())
            .toList();
            
        return new TokenValidationResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            roleNames
        );
    }


}