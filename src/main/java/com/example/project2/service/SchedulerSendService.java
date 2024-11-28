package com.example.project2.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerSendService {

    private boolean isLogin = false;
    private int minute = 0;

    private final JavaMailSender javaMailSender;

    public SchedulerSendService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void loginSuccess() {
        isLogin = true;
        minute = 0;
    }

    @Scheduled(fixedRate = 30000)
    public void sendLogin() {
        if (isLogin && minute < 5) {
            sendEmail();
            minute++;
        }

        if (minute >= 5) {
            isLogin = false;
        }
    }

    private void sendEmail() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("jbugh710@gmail.com");
            helper.setTo("jbugh710@naver.com");
            helper.setSubject("로그인");
            helper.setText("로그인을 시도 하였습니다!"+true);

            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}