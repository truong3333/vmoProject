package com.example.vmoProject.controller;

import com.example.vmoProject.domain.dto.request.MonthlyCostRequest;
import com.example.vmoProject.domain.dto.response.ApiResponses;
import com.example.vmoProject.domain.dto.response.MonthlyCostResponse;
import com.example.vmoProject.service.MonthlyCostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/monthly-cost")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Tag(name = "Monthly-Cost", description = "Quản lý chi phí hàng tháng")

public class MonthlyCostController {
    MonthlyCostService monthlyCostService;

    @PostMapping
    ApiResponses<String> createMonthlyCost(@RequestBody MonthlyCostRequest request) {
        return ApiResponses.<String>builder()
                .code(1000)
                .result(monthlyCostService.create(request))
                .build();
    }

    @GetMapping("/{roomNumber}")
    ApiResponses<List<MonthlyCostResponse>> getAllByRoomNumber(@Parameter(description = "Room number")@PathVariable String roomNumber) {
        return ApiResponses.<List<MonthlyCostResponse>>builder()
                .code(1000)
                .result(monthlyCostService.getAllByRoomNumber(roomNumber))
                .build();
    }

    @PutMapping
    ApiResponses<String> updateMonthlyCost(@RequestBody MonthlyCostRequest request) {
        return ApiResponses.<String>builder()
                .code(1000)
                .result(monthlyCostService.update(request))
                .build();
    }
}
