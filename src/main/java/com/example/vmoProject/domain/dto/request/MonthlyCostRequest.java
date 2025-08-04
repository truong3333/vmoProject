package com.example.vmoProject.domain.dto.request;


import com.example.vmoProject.domain.entity.Apartment;
import com.example.vmoProject.domain.entity.Cost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyCostRequest {

    String name;
    Double totalAmount;
    String statusPayment;

    String roomNumber;
}
