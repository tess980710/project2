package com.example.project2.service;

import com.example.project2.dto.ApiUserDto;
import com.example.project2.repo.ApiUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiUserService {

    @Autowired
    private ApiUserRepo apiUserRepo;

    public List<ApiUserDto> getAllUsers() {
        return apiUserRepo.findAll();
    }


    public Optional<ApiUserDto> findUserByID(String id) {
        return apiUserRepo.findById(id);
    }


    public ApiUserDto saveUser(ApiUserDto apiUserDto) {return apiUserRepo.save(apiUserDto);}


    public ApiUserDto updateUser(String id, ApiUserDto apiUserDto) {
        Optional<ApiUserDto> existingUser = apiUserRepo.findById(id);
        if (existingUser.isPresent()) {
            ApiUserDto user = existingUser.get();

            user.setName(apiUserDto.getName());
            user.setEmail(apiUserDto.getEmail());

            return apiUserRepo.save(user);
        } else {
            return null;
        }
    }

    public boolean UserDelete(String id) {
        Optional<ApiUserDto> userDto = apiUserRepo.findById(id);
        if (userDto.isPresent()) {
            apiUserRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
