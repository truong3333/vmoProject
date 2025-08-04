package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.AuthenticationRequest;
import com.example.vmoProject.domain.dto.response.ApiResponses;
import com.example.vmoProject.domain.dto.response.AuthenticationResponse;
import com.example.vmoProject.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Authentication", description = "Quản lý đăng nhập")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponses<AuthenticationResponse > authenticated(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponses.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }


}
