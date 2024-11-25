package com.example.project2.repo;

import com.example.project2.dto.ReservationDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserRepo extends JpaRepository<ReservationDto, Integer> {
    ReservationDto save(ReservationDto reservation);
}
