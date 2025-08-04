package com.example.vmoProject.repository;

import com.example.vmoProject.domain.entity.ApartmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentHistoryRepository extends JpaRepository<ApartmentHistory,String> {
    List<ApartmentHistory> findAllByApartment_roomNumber(String roomNumber);
    boolean existsByApartment_RoomNumberAndIsRepresentativeTrue(String roomNumber);
    boolean existsByApartment_RoomNumberAndUser_Username(String roomnumber,String username);
    Optional<ApartmentHistory> findByApartment_RoomNumberAndUser_Username(String roomNumber,String username);

}
