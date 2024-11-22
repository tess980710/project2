package com.example.project2.repo;

import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReserRepo extends JpaRepository<UserDto,Integer> {
    Optional<UserDto> findByIdAndPassword(String id, String password);

    boolean existsById(String id);

    List<UserDto> findByRole(Integer role);



}
