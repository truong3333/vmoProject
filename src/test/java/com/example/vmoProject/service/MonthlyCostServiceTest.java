package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.MonthlyCostRequest;
import com.example.vmoProject.domain.dto.response.MonthlyCostResponse;
import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.MonthlyCost;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.ApartmentRepository;
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
class MonthlyCostServiceTest {
    @Mock
    ApartmentRepository apartmentRepository;
    @Mock
    CostRepository costRepository;
    @Mock
    MonthlyCostRepository monthlyCostRepository;
    @InjectMocks
    MonthlyCostService monthlyCostService;

    @Test
    void testCreateMonthlyCost_ApartmentNotFound() {
        MonthlyCostRequest request = new MonthlyCostRequest();
        request.setRoomNumber("101");
        when(monthlyCostRepository.existsByName(any())).thenReturn(false);
        when(apartmentRepository.findByRoomNumber("101")).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> monthlyCostService.create(request));
        assertEquals(ErrorCode.APARTMENT_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void testGetAllByRoomNumber_Empty() {
        when(apartmentRepository.findByRoomNumber(any())).thenReturn(Optional.of(new Apartment()));
        when(monthlyCostRepository.findAllByApartment_RoomNumber(any())).thenReturn(Collections.emptyList());
        AppException ex = assertThrows(AppException.class, () -> monthlyCostService.getAllByRoomNumber("101"));
        assertEquals(ErrorCode.MONTHLY_COST_NULL, ex.getErrorCode());
    }
}

