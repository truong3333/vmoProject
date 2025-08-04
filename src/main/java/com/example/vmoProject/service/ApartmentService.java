package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.ApartmentRequest;
import com.example.vmoProject.domain.dto.response.*;
import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.ApartmentHistory;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.ApartmentHistoryRepository;
import com.example.vmoProject.repository.ApartmentRepository;
import com.example.vmoProject.repository.MonthlyCostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ApartmentService {
    ApartmentRepository apartmentRepository;
    ApartmentHistoryRepository apartmentHistoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "apartments", allEntries = true)
    public String create(ApartmentRequest request){
        if(apartmentRepository.existsByRoomNumber(request.getRoomNumber())){
            log.info("Apartment room number : {} already exists, create failed.",request.getRoomNumber());
            throw new AppException(ErrorCode.APARTMENT_EXISTED);
        }

        List<ApartmentHistory> listApartmentHistory = apartmentHistoryRepository.findAllByApartment_roomNumber(request.getRoomNumber());

        Apartment apartment = new Apartment();
        apartment.setRoomNumber(request.getRoomNumber());
        apartment.setArea(request.getArea());
        apartment.setAddress(request.getAddress());
        apartment.setListApartmentHistory(listApartmentHistory);

        apartmentRepository.save(apartment);
        log.info("Apartment room number {} create successfully.",request.getRoomNumber());

        return "Apartment room number " + request.getRoomNumber() + " create successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "apartments")
    public ApartmentResponse getByRoomNumber(String roomNumber){
        Apartment apartment = apartmentRepository.findByRoomNumber(roomNumber).orElseThrow(() -> {
            log.info("Apartment room number: {} not exists.", roomNumber);
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        log.info("Get info of apartment room number: " + roomNumber + " successfully.");

        return ApartmentResponse.builder()
                        .roomNumber(apartment.getRoomNumber())
                        .area(apartment.getArea())
                        .address(apartment.getAddress())
                        .listApartmentHistory(
                                apartment.getListApartmentHistory().stream().map(apartmentHistory ->
                                        ApartmentHistoryResponse.builder()
                                                .roomNumber(apartmentHistory.getApartment().getRoomNumber())
                                                .userResponse(new UserResponse(
                                                        apartmentHistory.getUser().getUsername(),
                                                        apartmentHistory.getUser().getUserDetail().getFullName()
                                                ))
                                                .isRepresentative(apartmentHistory.isRepresentative())
                                                .startDate(apartmentHistory.getStartDate())
                                                .endDate(apartmentHistory.getEndDate())
                                                .status(apartmentHistory.getStatus())
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .listMonthlyCost(
                                apartment.getListMonthlyCost().stream().map(monthlyCost ->
                                        MonthlyCostResponse.builder()
                                                .name(monthlyCost.getName())
                                                .totalAmount(monthlyCost.getTotalAmount())
                                                .dateCreate(monthlyCost.getDateCreate())
                                                .statusPayment(monthlyCost.getStatusPayment())
                                                .listCost(
                                                        monthlyCost.getListCost().stream().map(cost ->
                                                                CostResponse.builder()
                                                                        .type(cost.getType())
                                                                        .description(cost.getDescription())
                                                                        .amount(cost.getAmount())
                                                                        .build()
                                                        ).collect(Collectors.toList())
                                                )
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "apartments")
    public List<ApartmentResponse> getAll(){
        List<Apartment> listApartment = apartmentRepository.findAll();

        if(listApartment.isEmpty()){
            log.error("List Apartment is empty,get all apartment failed");
            throw new AppException(ErrorCode.APARTMENT_NULL);
        }

        log.info("Get list all apartment successfully.");

        return listApartment.stream().map(apartment ->
                ApartmentResponse.builder()
                        .roomNumber(apartment.getRoomNumber())
                        .area(apartment.getArea())
                        .address(apartment.getAddress())
                        .listApartmentHistory(
                                apartment.getListApartmentHistory().stream().map(apartmentHistory ->
                                        ApartmentHistoryResponse.builder()
                                                .roomNumber(apartmentHistory.getApartment().getRoomNumber())
                                                .userResponse(new UserResponse(
                                                        apartmentHistory.getUser().getUsername(),
                                                        apartmentHistory.getUser().getUserDetail().getFullName()
                                                ))
                                                .isRepresentative(apartmentHistory.isRepresentative())
                                                .startDate(apartmentHistory.getStartDate())
                                                .endDate(apartmentHistory.getEndDate())
                                                .status(apartmentHistory.getStatus())
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .listMonthlyCost(
                                apartment.getListMonthlyCost().stream().map(monthlyCost ->
                                        MonthlyCostResponse.builder()
                                                .name(monthlyCost.getName())
                                                .totalAmount(monthlyCost.getTotalAmount())
                                                .dateCreate(monthlyCost.getDateCreate())
                                                .statusPayment(monthlyCost.getStatusPayment())
                                                .listCost(
                                                        monthlyCost.getListCost().stream().map(cost ->
                                                                CostResponse.builder()
                                                                        .type(cost.getType())
                                                                        .description(cost.getDescription())
                                                                        .amount(cost.getAmount())
                                                                        .build()
                                                        ).collect(Collectors.toList())
                                                )
                                                .build()
                                ).collect(Collectors.toList())
                        )
                        .build()
        ).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String update(String roomNumber, ApartmentRequest request){
        Apartment apartment = apartmentRepository.findByRoomNumber(roomNumber).orElseThrow(() -> {
            log.error("Apartment room number: {} not exist, update failed.",roomNumber);
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        apartment.setArea(request.getArea());
        apartment.setAddress(request.getAddress());

        apartmentRepository.save(apartment);
        log.info("Apartment room number: {} update successfully.",roomNumber);

        return "Apartment room number: " + roomNumber + " update successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String delete(String roomNumber){
        if(!apartmentRepository.existsByRoomNumber(roomNumber)){
            log.error("Apartment room number: {} not exist, delete failed.",roomNumber);
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        }

        apartmentRepository.deleteById(roomNumber);
        log.info("Apartment room number: {} delete successfully.",roomNumber);

        return "Apartment room number: " + roomNumber +" delete successfully.";
    }
}
