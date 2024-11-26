package com.example.project2.repo;

import com.example.project2.dto.StoreDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepo extends JpaRepository<StoreDto, Integer> {
    List<StoreDto> findAll();

    void deleteById(Integer id);

    List<StoreDto> findByUserid(String id);
}
