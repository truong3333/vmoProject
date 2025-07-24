package com.example.vmoProject.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    int electricityUsage;
    int waterUsage;
    Double electricityCost;
    Double waterCost;
    Double otherCost;
    String description;
    Double totalCost;
    LocalDate paymentDate;
    String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    Apartment apartment;
}
