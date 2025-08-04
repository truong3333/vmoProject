package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.PermissionRequest;
import com.example.vmoProject.domain.dto.response.PermissionResponse;
import com.example.vmoProject.domain.entity.Permission;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
    @Mock
    PermissionRepository permissionRepository;
    @InjectMocks
    PermissionService permissionService;

    @Test
    void testCreatePermission_Success() {
        PermissionRequest request = new PermissionRequest();
        request.setName("VIEW");
        when(permissionRepository.existsByName("VIEW")).thenReturn(false);
        when(permissionRepository.save(any(Permission.class))).thenReturn(new Permission());
        String result = permissionService.create(request);
        assertTrue(result.contains("create successfully"));
    }

    @Test
    void testCreatePermission_AlreadyExists() {
        PermissionRequest request = new PermissionRequest();
        request.setName("VIEW");
        when(permissionRepository.existsByName("VIEW")).thenReturn(true);
        AppException ex = assertThrows(AppException.class, () -> permissionService.create(request));
        assertEquals(ErrorCode.PERMISSION_EXISTED, ex.getErrorCode());
    }

    @Test
    void testGetAllPermissions_Success() {
        Permission permission = new Permission("VIEW", "desc");
        when(permissionRepository.findAll()).thenReturn(List.of(permission));
        List<PermissionResponse> result = permissionService.getAll();
        assertEquals(1, result.size());
        assertEquals("VIEW", result.get(0).getName());
    }

    @Test
    void testGetAllPermissions_Empty() {
        when(permissionRepository.findAll()).thenReturn(Collections.emptyList());
        AppException ex = assertThrows(AppException.class, () -> permissionService.getAll());
        assertEquals(ErrorCode.PERMISSION_NULL, ex.getErrorCode());
    }
}

