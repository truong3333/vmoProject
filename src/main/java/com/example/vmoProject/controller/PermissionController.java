package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.PermissionRequest;
import com.example.vmoProject.domain.dto.response.ApiResponses;
import com.example.vmoProject.domain.dto.response.PermissionResponse;
import com.example.vmoProject.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Permission", description = "Quản lý quyền hệ thống")
public class PermissionController {
    PermissionService permissionService;

    @Operation(summary = "Tạo mới quyền", description = "Tạo mới một quyền cho hệ thống")
    @ApiResponse(responseCode = "200", description = "Tạo permission thành công")
    @PostMapping
    ApiResponses<String> createPermission(@RequestBody PermissionRequest request){
        return ApiResponses.<String>builder()
                .result(permissionService.create(request))
                .build();
    }

    @Operation(summary = "Lấy danh sách quyền", description = "Lấy tất cả quyền trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Danh sách permission")
    @GetMapping
    ApiResponses<List<PermissionResponse>> getAllPermission(){
        return ApiResponses.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @Operation(summary = "Xóa quyền", description = "Xóa quyền theo tên")
    @ApiResponse(responseCode = "200", description = "Xóa permission thành công")
    @DeleteMapping("/{permissionName}")
    ApiResponses<String> deletePermission(@Parameter(description = "Tên quyền") @PathVariable String permissionName){
        return ApiResponses.<String>builder()
                .result("Delete permission successfully!")
                .build();
    }
}
