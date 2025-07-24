package com.example.vmoProject.domain.dto.response;

import com.example.vmoProject.domain.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String fullName;
    String email;
    String phone;

    Set<Role> roles;
}
