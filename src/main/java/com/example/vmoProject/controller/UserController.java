package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.UserCreateRequest;
import com.example.vmoProject.domain.dto.request.UserUpdateRequest;
import com.example.vmoProject.domain.dto.response.ApiResponse;
import com.example.vmoProject.domain.dto.response.UserResponse;
import com.example.vmoProject.domain.dto.response.UserResponseForAdmin;
import com.example.vmoProject.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> create(@RequestBody @Valid UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();

    }

    @GetMapping
    ApiResponse<List<UserResponseForAdmin>> getAll(){
        return ApiResponse.<List<UserResponseForAdmin>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> delete(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("Delete user success")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> update(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }
}
