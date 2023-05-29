package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.utils.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EmailController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private final JavaMailSender javaMailSender;

    public EmailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/send-email")
    @ResponseBody
    public ResponseEntity<String> sendEmail(HttpServletResponse response, @RequestParam String accountNumber) {
        try {
            // Obtener la cuenta
            Account account = accountRepository.findByNumber(accountNumber);

            // Obtener la lista de transacciones
            List<TransactionDTO> transactionDTOList = account.getTransactions().stream()
                    .map(TransactionDTO::new)
                    .collect(Collectors.toList());

            // Generar el PDF utilizando PDFGenerator
            PDFGenerator pdfGenerator = new PDFGenerator(transactionDTOList);
            byte[] pdfBytes = pdfGenerator.export();

            // Preparar el archivo adjunto
            ByteArrayResource attachment = new ByteArrayResource(pdfBytes) {
                @Override
                public String getFilename() {
                    return "transactions.pdf";
                }
            };

            // Preparar el mensaje de correo
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("cesar.cssoto@gmail.com"); // Reemplaza con la dirección de correo del destinatario
            helper.setSubject("Transacciones");
            helper.setText(getEmailTemplate(), true); // Utilizar la plantilla HTML como cuerpo del correo

            // Adjuntar el archivo PDF
            helper.addAttachment(attachment.getFilename(), attachment);

            // Enviar el correo electrónico
            javaMailSender.send(message);

            return ResponseEntity.ok("Correo enviado exitosamente.");
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace(); // Imprime el mensaje de error en la consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
        }
    }

    private String getEmailTemplate() throws IOException {
        ClassPathResource emailResource = new ClassPathResource("static/web/html/email.html");
        try (InputStream inputStream = emailResource.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
