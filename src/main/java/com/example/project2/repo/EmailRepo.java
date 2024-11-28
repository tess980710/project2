package com.example.project2.repo;

import com.example.project2.dto.MailDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmailRepo {

    private final List<MailDto> emailList = new ArrayList<>();

    public void saveEmail(MailDto mailDto) {
        emailList.add(mailDto);
    }

    public List<MailDto> getAllEmails() {
        return emailList;
    }
}
