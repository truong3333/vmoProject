package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.ApartmentRequest;
import com.example.vmoProject.domain.dto.response.ApartmentResponse;
import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.ApartmentHistoryRepository;
import com.example.vmoProject.repository.ApartmentRepository;
import com.example.vmoProject.repository.MonthlyCostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceTest {
    @Mock
    ApartmentRepository apartmentRepository;
    @Mock
    ApartmentHistoryRepository apartmentHistoryRepository;
    @Mock
    MonthlyCostRepository monthlyCostRepository;
    @InjectMocks
    ApartmentService apartmentService;

    @Test
    void testCreateApartment_Success() {
        ApartmentRequest request = new ApartmentRequest();
        request.setRoomNumber("101");
        when(apartmentRepository.existsByRoomNumber("101")).thenReturn(false);
        when(apartmentHistoryRepository.findAllByApartment_roomNumber(any())).thenReturn(Collections.emptyList());
        when(apartmentRepository.save(any(Apartment.class))).thenReturn(new Apartment());
        String result = apartmentService.create(request);
        assertTrue(result.contains("create successfully"));
    }

    @Test
    void testCreateApartment_AlreadyExists() {
        ApartmentRequest request = new ApartmentRequest();
        request.setRoomNumber("101");
        when(apartmentRepository.existsByRoomNumber("101")).thenReturn(true);
        AppException ex = assertThrows(AppException.class, () -> apartmentService.create(request));
        assertEquals(ErrorCode.APARTMENT_EXISTED, ex.getErrorCode());
    }

    @Test
    void testGetAllApartments_Empty() {
        when(apartmentRepository.findAll()).thenReturn(Collections.emptyList());
        AppException ex = assertThrows(AppException.class, () -> apartmentService.getAll());
        assertEquals(ErrorCode.APARTMENT_NULL, ex.getErrorCode());
    }
}

