package com.example.vmoProject.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApartmentRequest {
    String roomNumber;
    Double area;
    String address;
}
