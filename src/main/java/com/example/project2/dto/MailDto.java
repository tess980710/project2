package com.example.project2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailDto {
    private String emailAddr;
    private String emailTilte;
    private String emailContent;
}
