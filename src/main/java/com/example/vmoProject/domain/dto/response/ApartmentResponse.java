package com.example.vmoProject.domain.dto.response;

import com.example.vmoProject.domain.entity.MonthlyCost;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApartmentResponse {
    String roomNumber;
    Double area;
    String address;

    List<ApartmentHistoryResponse> listApartmentHistory;
    List<MonthlyCostResponse> listMonthlyCost;
}
