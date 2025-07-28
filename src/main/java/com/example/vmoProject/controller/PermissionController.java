package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.PermissionRequest;
import com.example.vmoProject.domain.dto.response.ApiResponse;
import com.example.vmoProject.domain.dto.response.PermissionResponse;
import com.example.vmoProject.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermission(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

//    @PutMapping("/{permissionName}")
//    ApiResponse<PermissionResponse> updatePermission(@PathVariable String permissionName,@RequestBody PermissionRequest request){
//        return ApiResponse.<PermissionResponse>builder()
//                .result(permissionService.update(permissionName, request))
//                .build();
//    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<String> deletePermission(@PathVariable String permissionName){
        return ApiResponse.<String>builder()
                .result("Delete permission successfully!")
                .build();
    }
}
