package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.ApartmentHistoryRequest;
import com.example.vmoProject.domain.dto.response.ApartmentHistoryResponse;
import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.User;
import com.example.vmoProject.domain.entity.ApartmentHistory;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.ApartmentHistoryRepository;
import com.example.vmoProject.repository.ApartmentRepository;
import com.example.vmoProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApartmentHistoryServiceTest {
    @Mock
    ApartmentRepository apartmentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ApartmentHistoryRepository apartmentHistoryRepository;
    @InjectMocks
    ApartmentHistoryService apartmentHistoryService;

    @Test
    void testCreateApartmentHistory_Success() {
        ApartmentHistoryRequest request = new ApartmentHistoryRequest();
        request.setRoomNumber("101");
        request.setUsername("user1");

        when(apartmentRepository.findByRoomNumber("101")).thenReturn(Optional.of(new Apartment()));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(new User()));
        when(apartmentHistoryRepository.existsByApartment_RoomNumberAndUser_Username(any(), any())).thenReturn(false);
        when(apartmentHistoryRepository.save(any(ApartmentHistory.class))).thenReturn(new ApartmentHistory());
        String result = apartmentHistoryService.create(request);
        assertTrue(result.contains("successfully"));
    }

    @Test
    void testCreateApartmentHistory_ApartmentNotFound() {
        ApartmentHistoryRequest request = new ApartmentHistoryRequest();
        request.setRoomNumber("101");
        when(apartmentRepository.findByRoomNumber("101")).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> apartmentHistoryService.create(request));
        assertEquals(ErrorCode.APARTMENT_NOT_EXISTED, ex.getErrorCode());
    }
}

