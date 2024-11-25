package com.example.project2.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "reservation")
public class ReservationDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    private String date;


    private Integer phone;

    @ManyToOne
    @JoinColumn(name = "storeid")
    private StoreDto store;


    @ManyToOne
    @JoinColumn(name = "userid")
    private UserDto user;

}
