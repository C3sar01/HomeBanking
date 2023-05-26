package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class EmailController {
    private MailService mailService;
    public void sendPdfEmail(){
        mailService.SendListEmail("nejobac@gmail.com");
    }
}
