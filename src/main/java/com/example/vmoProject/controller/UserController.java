package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.UserCreateRequest;
import com.example.vmoProject.domain.dto.request.UserUpdateRequest;
import com.example.vmoProject.domain.dto.response.ApiResponses;
import com.example.vmoProject.domain.dto.response.UserResponse;
import com.example.vmoProject.domain.dto.response.UserResponseForAdmin;
import com.example.vmoProject.service.UserService;
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

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "User", description = "Quản lý người dùng")
public class UserController {
    UserService userService;

    @Operation(summary = "Tạo mới người dùng", description = "Tạo mới một người dùng với thông tin truyền vào")
    @ApiResponse(responseCode = "200", description = "Tạo user thành công")
    @PostMapping
    ApiResponses<UserResponse> create(@RequestBody @Valid UserCreateRequest request){
        return ApiResponses.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();

    }

    @Operation(summary = "Lấy danh sách người dùng", description = "Lấy tất cả người dùng cho admin",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Danh sách user")
    @GetMapping
    ApiResponses<List<UserResponseForAdmin>> getAll(){
        return ApiResponses.<List<UserResponseForAdmin>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @Operation(summary = "Xem profile", description = "Xem profile ",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "profile")
    @GetMapping("/{myInfo}")
    ApiResponses<UserResponse> getMyInfo(){
        return ApiResponses.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @Operation(summary = "Xóa người dùng", description = "Xóa người dùng theo userId",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Xóa user thành công")
    @DeleteMapping("/{userId}")
    ApiResponses<String> delete(@Parameter(description = "ID người dùng") @PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponses.<String>builder()
                .result("Delete user success")
                .build();
    }

    @Operation(summary = "Cập nhật người dùng", description = "Cập nhật thông tin người dùng theo userId",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Cập nhật user thành công")
    @PutMapping("/{userId}")
    ApiResponses<UserResponse> update(@Parameter(description = "ID người dùng") @PathVariable String userId, @RequestBody @Valid UserUpdateRequest request){
        return ApiResponses.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }
}
