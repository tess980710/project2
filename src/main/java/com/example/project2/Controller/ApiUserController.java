package com.example.project2.Controller;

import com.example.project2.dto.ApiUserDto;
import com.example.project2.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private ApiUserService apiUserService;

    @GetMapping
    public List<ApiUserDto> getAllUser() {
        return apiUserService.getAllUsers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiUserDto> getUser(@PathVariable String id) {
        Optional<ApiUserDto> userDto = apiUserService.findUserByID(id);
        if (userDto.isPresent()) {
            return ResponseEntity.ok(userDto.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @PostMapping
    public ResponseEntity<ApiUserDto> createUser(@RequestBody ApiUserDto apiUserDto) {
        ApiUserDto saveUser = apiUserService.saveUser(apiUserDto);
        return ResponseEntity.status(201).body(saveUser);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiUserDto> updateUser(@PathVariable String id, @RequestBody ApiUserDto apiUserDto) {
        ApiUserDto updatedUser = apiUserService.updateUser(id, apiUserDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        boolean isDeleted = apiUserService.UserDelete(id);
        if(isDeleted){
            return ResponseEntity.ok("성공");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
