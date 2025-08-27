package com.interbanking.autentication.infrastructure.adapter.in.web.response;

import java.time.LocalDateTime;
import java.util.List;
import com.interbanking.commons.models.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private List<String> roles;
    private LocalDateTime createdAt;
    private boolean enabled;

  
    public static UserResponse from(UserEntity user) {
        List<String> roleNames = user.getRoles().stream()
            .map(role -> role.getName())
            .toList();
            
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            roleNames,
            user.getCreated_at(),
            user.isEnabled()
        );
    }

   
}