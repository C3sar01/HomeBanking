package com.mindhub.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PDFGeneratorService pdfGeneratorService;
    @Value("${spring.mail.service}")
    private String email;

    public void SendListEmail(String emailTo){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        /*aqui es donde se adjunta el pdf llamaando al service*/
            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject("a");
            helper.setText("cuerpo del correo?");
            /*helper.addAttachment("",); aqui es donde iria el nombre del archivo con el archivo???*/
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
