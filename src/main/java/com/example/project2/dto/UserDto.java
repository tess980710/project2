package com.example.project2.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "user")
public class UserDto {

    @Id
    private String id;

    private String password;

    private String name;

    private String phonenum;

    private String email;

    private Integer role;
}
