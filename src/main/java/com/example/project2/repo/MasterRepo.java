package com.example.project2.repo;

import com.example.project2.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MasterRepo extends JpaRepository<UserDto, String> {

    Optional<UserDto> findById(String id);

    UserDto getUserById(String id);
}
