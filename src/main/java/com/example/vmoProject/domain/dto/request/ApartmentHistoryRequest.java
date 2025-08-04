package com.example.vmoProject.domain.dto.request;

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
public class ApartmentHistoryRequest {

    String roomNumber;
    String username;
    boolean isRepresentative;
    LocalDate startDate;
    LocalDate endDate;
    String status;

}
