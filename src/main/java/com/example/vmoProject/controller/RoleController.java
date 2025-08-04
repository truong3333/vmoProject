package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.RoleRequest;
import com.example.vmoProject.domain.dto.response.ApiResponses;
import com.example.vmoProject.domain.dto.response.RoleResponse;
import com.example.vmoProject.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role", description = "Quản lý vai trò người dùng")
public class RoleController {
    RoleService roleService;

    @Operation(summary = "Tạo mới vai trò", description = "Tạo mới một vai trò cho hệ thống")
    @ApiResponse(responseCode = "200", description = "Tạo role thành công")
    @PostMapping
    ApiResponses<String> create(@RequestBody @Valid RoleRequest request) {
        return ApiResponses.<String>builder()
                .result(roleService.create(request))
                .build();
    }

    @Operation(summary = "Lấy danh sách vai trò", description = "Lấy tất cả vai trò trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Danh sách role")
    @GetMapping
    ApiResponses<List<RoleResponse>> getAll() {
        return ApiResponses.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @Operation(summary = "Xóa vai trò", description = "Xóa vai trò theo tên")
    @ApiResponse(responseCode = "200", description = "Xóa role thành công")
    @DeleteMapping("/{roleName}")
    ApiResponses<String> delete(@Parameter(description = "Tên vai trò") @PathVariable String roleName) {
        roleService.delete(roleName);
        return ApiResponses.<String>builder()
                .result("Delete role success")
                .build();
    }

}
