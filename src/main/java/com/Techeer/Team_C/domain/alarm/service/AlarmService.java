package com.Techeer.Team_C.domain.alarm.service;

import com.Techeer.Team_C.domain.alarm.dto.MailDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlarmService {

    private JavaMailSender emailSender;

    public void sendSimpleMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("techeerteamc@gmail.com");
        message.setTo("ashyonu@gmail.com");
        message.setSubject("test");
        message.setText("test");
        emailSender.send(message);
    }
}
