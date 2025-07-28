package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.RoleRequest;
import com.example.vmoProject.domain.dto.response.ApiResponse;
import com.example.vmoProject.domain.dto.response.RoleResponse;
import com.example.vmoProject.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody @Valid RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleName}")
    ApiResponse<String> delete(@PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiResponse.<String>builder()
                .result("Delete role success")
                .build();
    }

}
