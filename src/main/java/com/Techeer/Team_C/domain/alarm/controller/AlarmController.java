package com.Techeer.Team_C.domain.alarm.controller;

import com.Techeer.Team_C.domain.alarm.dto.MailDto;
import com.Techeer.Team_C.domain.alarm.service.AlarmService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlarmController {

    private final AlarmService emailService;

    public AlarmController(AlarmService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/mail/send")
    public String sendMail(MailDto mailDto) {
        emailService.sendSimpleMessage(mailDto);
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);

        return obj.toString();
    }
}
