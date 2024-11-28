package com.example.project2.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "apiuser")
public class ApiUserDto {

    @Id
    private String id;

    private String name;
    private String email;
}
