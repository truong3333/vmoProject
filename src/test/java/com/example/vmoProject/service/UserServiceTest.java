package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.UserCreateRequest;
import com.example.vmoProject.domain.dto.request.UserUpdateRequest;
import com.example.vmoProject.domain.dto.response.UserResponse;
import com.example.vmoProject.domain.entity.User;
import com.example.vmoProject.domain.entity.UserDetail;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.RoleRepository;
import com.example.vmoProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void testCreateUser_Success() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(roleRepository.findAllById(any())).thenReturn(java.util.List.of());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        String encoded = "encodedPass";
        when(passwordEncoder.encode(any())).thenReturn(encoded);
        UserResponse response = userService.createUser(request);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void testCreateUser_AlreadyExists() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser");
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        AppException ex = assertThrows(AppException.class, () -> userService.createUser(request));
        assertEquals(ErrorCode.USER_EXISTED, ex.getErrorCode());
    }

}

