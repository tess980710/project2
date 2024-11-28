package com.example.project2.repo;

import com.example.project2.dto.ApiUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserRepo extends JpaRepository<ApiUserDto, String> {

}
