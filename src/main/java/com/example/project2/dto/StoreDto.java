package com.example.project2.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "store")
public class StoreDto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String title;

        private  String description;
        private  String photo;

        private  Integer num;

        private LocalDate createdtime;

        private  LocalDate updatedtime;

        private  String userid;
}
