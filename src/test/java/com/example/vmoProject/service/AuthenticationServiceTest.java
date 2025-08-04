package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.AuthenticationRequest;
import com.example.vmoProject.domain.dto.response.AuthenticationResponse;
import com.example.vmoProject.domain.entity.User;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
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
class AuthenticationServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void testAuthenticate_UserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> authenticationService.authenticate(request));
        assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void testAuthenticate_WrongPassword() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("user1");
        request.setPassword("wrong");
        User user = new User();
        user.setPassword("encoded");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        AppException ex = assertThrows(AppException.class, () -> authenticationService.authenticate(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, ex.getErrorCode());
    }
}

