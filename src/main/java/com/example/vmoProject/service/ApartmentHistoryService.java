package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.ApartmentHistoryRequest;
import com.example.vmoProject.domain.dto.response.ApartmentHistoryResponse;
import com.example.vmoProject.domain.dto.response.UserResponse;
import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.ApartmentHistory;
import com.example.vmoProject.domain.entity.User;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.ApartmentHistoryRepository;
import com.example.vmoProject.repository.ApartmentRepository;
import com.example.vmoProject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ApartmentHistoryService {
    ApartmentRepository apartmentRepository;
    UserRepository userRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "apartmentHistories", allEntries = true)
    public String create(ApartmentHistoryRequest request){

        Apartment apartment = apartmentRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(() -> {
            log.error("Apartment room number {} not exists, create resident to apartment failed.",request.getRoomNumber());
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            log.error("Username: {} not exists, create resident to apartment failed.",request.getUsername());
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        });

        if (request.isRepresentative() && apartmentHistoryRepository.existsByApartment_RoomNumberAndIsRepresentativeTrue(request.getRoomNumber())) {
            log.error("Apartment room number: {} has a representative, create resident to apartment failed.", request.getRoomNumber());
            throw new AppException(ErrorCode.ISREPRESENTATIVE_EXISTED);
        }

        if(apartmentHistoryRepository.existsByApartment_RoomNumberAndUser_Username(request.getRoomNumber(),request.getUsername())){
            log.error("Resident already existed in apartment, create apartment history failed.");
            throw new AppException(ErrorCode.APARTMENT_HISTORY_EXISTED);
        }

        ApartmentHistory apartmentHistory = ApartmentHistory.builder()
                .apartment(apartment)
                .user(user)
                .isRepresentative(request.isRepresentative())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .build();

        apartment.getListApartmentHistory().add(apartmentHistory);
        apartmentHistoryRepository.save(apartmentHistory);
        log.info("Create resident to the apartment: {} successfully.",request.getRoomNumber());

        return "Create resident to the apartment successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "apartmentHistories")
    public List<ApartmentHistoryResponse> getAll(){
        List<ApartmentHistory> listApartmentHistory = apartmentHistoryRepository.findAll();
        if(listApartmentHistory.isEmpty()){
            log.error("List apartment history is empty, get all apartment history failed.");
            throw new AppException(ErrorCode.APARTMENT_HISTORY_NULL);
        }

        log.info("Get all list apartment history successfully.");

        return listApartmentHistory.stream().map(apartmentHistory -> {
            return ApartmentHistoryResponse.builder()
                    .roomNumber(apartmentHistory.getApartment().getRoomNumber())
                    .userResponse(new UserResponse(apartmentHistory.getUser().getUsername(),apartmentHistory.getUser().getUserDetail().getFullName()))
                    .isRepresentative(apartmentHistory.isRepresentative())
                    .startDate(apartmentHistory.getStartDate())
                    .endDate(apartmentHistory.getEndDate())
                    .status("action")
                    .build();
        }).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String update(ApartmentHistoryRequest request){
        ApartmentHistory apartmentHistory = apartmentHistoryRepository.findByApartment_RoomNumberAndUser_Username(request.getRoomNumber(),request.getUsername()).orElseThrow(() -> {
            log.error("Apartment history not existed, update failed.");
            throw new AppException(ErrorCode.APARTMENT_HISTORY_NOT_EXISTED);
        });

        if (!apartmentHistory.isRepresentative() && request.isRepresentative() && apartmentHistoryRepository.existsByApartment_RoomNumberAndIsRepresentativeTrue(request.getRoomNumber())) {
            log.error("Apartment room number: {} has a representative, update resident to apartment failed.", request.getRoomNumber());
            throw new AppException(ErrorCode.ISREPRESENTATIVE_EXISTED);
        }

        apartmentHistory.setStatus(request.getStatus());
        apartmentHistory.setRepresentative(request.isRepresentative());
        if(apartmentHistory.getStatus() == "out") {
            apartmentHistory.setRepresentative(false);
        }

        apartmentHistory.setEndDate(request.getEndDate());

        apartmentHistoryRepository.save(apartmentHistory);
        log.info("Update apartment history for room number {} successfully.",apartmentHistory.getApartment().getRoomNumber());
        return "Update apartment history successfully";
    }
}
