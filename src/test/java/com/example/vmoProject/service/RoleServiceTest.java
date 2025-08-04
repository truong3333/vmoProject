package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.RoleRequest;
import com.example.vmoProject.domain.dto.response.RoleResponse;
import com.example.vmoProject.domain.entity.Role;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.PermissionRepository;
import com.example.vmoProject.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    RoleRepository roleRepository;
    @Mock
    PermissionRepository permissionRepository;
    @InjectMocks
    RoleService roleService;

    @Test
    void testCreateRole_Success() {
        RoleRequest request = new RoleRequest();
        request.setName("ADMIN");
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        when(permissionRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(roleRepository.save(any(Role.class))).thenReturn(new Role());
        String result = roleService.create(request);
        assertTrue(result.contains("create successfully"));
    }

    @Test
    void testCreateRole_AlreadyExists() {
        RoleRequest request = new RoleRequest();
        request.setName("ADMIN");
        when(roleRepository.existsByName("ADMIN")).thenReturn(true);
        AppException ex = assertThrows(AppException.class, () -> roleService.create(request));
        assertEquals(ErrorCode.ROLE_EXISTED, ex.getErrorCode());
    }

    @Test
    void testGetAllRoles_Success() {
        Role role = new Role();
        role.setName("RESIDENT");
        when(roleRepository.findAll()).thenReturn(List.of(role));
        List<RoleResponse> result = roleService.getAll();
        assertEquals(1, result.size());
        assertEquals("RESIDENT", result.get(0).getName());
    }

    @Test
    void testGetAllRoles_Empty() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());
        AppException ex = assertThrows(AppException.class, () -> roleService.getAll());
        assertEquals(ErrorCode.ROLE_NULL, ex.getErrorCode());
    }

    @Test
    void testDeleteRole_Success() {
        when(roleRepository.existsByName("ADMIN")).thenReturn(true);
        doNothing().when(roleRepository).deleteById("ADMIN");
        assertDoesNotThrow(() -> roleService.delete("ADMIN"));
    }

    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        AppException ex = assertThrows(AppException.class, () -> roleService.delete("ADMIN"));
        assertEquals(ErrorCode.ROLE_NOT_FOUND, ex.getErrorCode());
    }
}

