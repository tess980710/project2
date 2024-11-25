package com.example.project2.repo;

import com.example.project2.dto.ReservationDto;
import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserRepo extends JpaRepository<ReservationDto, Integer> {
    ReservationDto save(ReservationDto reservation);

    List<ReservationDto> findByStoreUserid(String id);

    List<ReservationDto> findAll();

    List<ReservationDto> findByStore(StoreDto store);

   void deleteById(Integer id);


    List<ReservationDto> findByUserId(String id);
}
