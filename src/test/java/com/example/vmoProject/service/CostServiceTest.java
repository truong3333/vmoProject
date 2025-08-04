package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.CostRequest;
import com.example.vmoProject.domain.dto.response.CostResponse;
import com.example.vmoProject.domain.entity.Cost;
import com.example.vmoProject.domain.entity.MonthlyCost;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.CostRepository;
import com.example.vmoProject.repository.MonthlyCostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CostServiceTest {
    @Mock
    CostRepository costRepository;
    @Mock
    MonthlyCostRepository monthlyCostRepository;
    @InjectMocks
    CostService costService;

    @Test
    void testCreateCost_Success() {
        CostRequest request = new CostRequest();
        request.setType("WATER");
        request.setMonthlyCost_Name("MC1");
        request.setAmount(60000.0);

        MonthlyCost monthlyCost = new MonthlyCost();
        monthlyCost.setTotalAmount(0.0);

        when(costRepository.existsByTypeAndMonthlyCost_Name(any(), any())).thenReturn(false);
        when(monthlyCostRepository.findByName(any())).thenReturn(Optional.of(monthlyCost));
        when(costRepository.save(any(Cost.class))).thenReturn(new Cost());
        String result = costService.create(request);
        assertTrue(result.contains("create successfully"));
    }

    @Test
    void testCreateCost_AlreadyExists() {
        CostRequest request = new CostRequest();
        request.setType("WATER");
        request.setMonthlyCost_Name("MC1");
        when(costRepository.existsByTypeAndMonthlyCost_Name(any(), any())).thenReturn(true);
        AppException ex = assertThrows(AppException.class, () -> costService.create(request));
        assertEquals(ErrorCode.COST_EXISTED, ex.getErrorCode());
    }

    @Test
    void testGetAllByMonthly_Empty() {
        when(costRepository.findAllByMonthlyCost_Name(any())).thenReturn(Collections.emptyList());
        List<CostResponse> result = costService.getAllByMonthly("MC1");
        assertTrue(result.isEmpty());
    }
}

