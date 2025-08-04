package com.example.vmoProject.domain.dto.response;

import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApartmentHistoryResponse {

    String roomNumber;
    UserResponse userResponse;
    boolean isRepresentative;
    LocalDate startDate;
    LocalDate endDate;
    String status;

}
