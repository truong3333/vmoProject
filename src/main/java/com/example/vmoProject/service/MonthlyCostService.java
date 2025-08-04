package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.MonthlyCostRequest;
import com.example.vmoProject.domain.dto.response.CostResponse;
import com.example.vmoProject.domain.dto.response.MonthlyCostResponse;
import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.Cost;
import com.example.vmoProject.domain.entity.MonthlyCost;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.ApartmentRepository;
import com.example.vmoProject.repository.CostRepository;
import com.example.vmoProject.repository.MonthlyCostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class MonthlyCostService {
    ApartmentRepository apartmentRepository;
    CostRepository costRepository;
    MonthlyCostRepository monthlyCostRepository;

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "monthlyCosts", allEntries = true)
    public String create(MonthlyCostRequest request){
        if(monthlyCostRepository.existsByName(request.getRoomNumber() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear())){
            log.error("Monthly cost name: {} already exists,create failed.",request.getName());
            throw new AppException(ErrorCode.MONTHLY_COST_EXISTED);
        }

        Apartment apartment = apartmentRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(() -> {
            log.error("Apartment room number : {} not exists, create monthly cost failed.",request.getRoomNumber());
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        List<Cost> listCost = costRepository.findAllByMonthlyCost_Name(request.getName());

        MonthlyCost monthlyCost = MonthlyCost.builder()
                .name(request.getRoomNumber() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear())
                .totalAmount(listCost.stream().mapToDouble(Cost::getAmount).sum())
                .dateCreate(LocalDate.now())
                .statusPayment(request.getStatusPayment())
                .listCost(listCost)
                .apartment(apartment)
                .build();

        apartment.getListMonthlyCost().add(monthlyCost);
        monthlyCostRepository.save(monthlyCost);
        log.info("Monthly cost of room number: {} create successfully.",request.getRoomNumber());

        return "Monthly cost create successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @Cacheable(value = "monthlyCosts", key = "#roomNumber")
    public List<MonthlyCostResponse> getAllByRoomNumber(String roomNumber) {
        Apartment apartment = apartmentRepository.findByRoomNumber(roomNumber).orElseThrow(() -> {
            log.error("Apartment room number : {} not exists, get monthly cost failed.", roomNumber);
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        List<MonthlyCost> listMonthlyCost = monthlyCostRepository.findAllByApartment_RoomNumber(roomNumber);

        if(listMonthlyCost.isEmpty()){
            log.error("List monthly cost of room number {} is empty, get all failed.",roomNumber);
            throw new AppException(ErrorCode.MONTHLY_COST_NULL);
        }

        return listMonthlyCost.stream().map(monthlyCost ->
                MonthlyCostResponse.builder()
                .name(monthlyCost.getName())
                .totalAmount(monthlyCost.getTotalAmount())
                .dateCreate(monthlyCost.getDateCreate())
                .statusPayment(monthlyCost.getStatusPayment())
                .listCost(monthlyCost.getListCost().stream().map(cost ->
                        CostResponse.builder()
                        .type(cost.getType())
                        .description(cost.getDescription())
                        .amount(cost.getAmount())
                        .build()).toList())
                .build()).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "monthlyCosts", allEntries = true)
    public String update(MonthlyCostRequest request) {
        MonthlyCost monthlyCost = monthlyCostRepository.findByName(request.getName()).orElseThrow(() -> {
            log.error("Monthly cost with name: {} not exists, update failed.", request.getName());
            throw new AppException(ErrorCode.MONTHLY_COST_NOT_EXISTED);
        });

        Apartment apartment = apartmentRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(() -> {
            log.error("Apartment room number : {} not exists, update monthly cost failed.", request.getRoomNumber());
            throw new AppException(ErrorCode.APARTMENT_NOT_EXISTED);
        });

        monthlyCost.setStatusPayment(request.getStatusPayment());

        monthlyCostRepository.save(monthlyCost);
        log.info("Monthly cost with name: {} updated successfully.", request.getName());

        return "Monthly cost updated successfully";
    }

}
