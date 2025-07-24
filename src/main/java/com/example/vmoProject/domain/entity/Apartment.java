package com.example.vmoProject.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Apartment {
    @Id
    String roomNumber;
    Double area;
    String address;

    @OneToMany(mappedBy = "apartment")
    List<ApartmentHistory> listResidents;
    
    @OneToMany(mappedBy = "apartment")
    List<Cost> listCosts;

}
