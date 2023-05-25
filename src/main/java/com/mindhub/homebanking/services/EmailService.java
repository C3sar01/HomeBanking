package com.mindhub.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.File;

/*
@Service
@Transactional
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String email;

    public void sendListEmail(String emailTo, File file) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailTo);
            helper.setSubject("PDF Attachment");
            helper.setText("Please find the attached PDF.");

            // Adjuntar el archivo PDF al correo electrónico
            FileSystemResource fileResource = new FileSystemResource(file);
            helper.addAttachment("pdf" + System.currentTimeMillis() + ".pdf", fileResource);

            // Enviar el correo electrónico
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}*/
/*  ********************/
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendListEmail(String emailTo, byte[] pdfContent, String filename) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailTo);
            helper.setSubject("PDF Attachment");
            helper.setText("Please find the attached PDF.");

            ByteArrayResource attachmentResource = new ByteArrayResource(pdfContent);
            helper.addAttachment(filename, attachmentResource);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
