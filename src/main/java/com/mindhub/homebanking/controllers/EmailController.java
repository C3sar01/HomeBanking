package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmailController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final JavaMailSender javaMailSender;

    public EmailController(JavaMailSender javaMailSender, TransactionRepository transactionRepository) {
        this.javaMailSender = javaMailSender;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/send-email")
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
            helper.setSubject("Transactions");
            helper.setText("Adjunto encontrarás el archivo PDF con las transacciones.");

            // Adjuntar el archivo PDF
            helper.addAttachment(attachment.getFilename(), attachment);

            // Enviar el correo electrónico
            javaMailSender.send(message);

            return ResponseEntity.ok("Correo enviado exitosamente.");
        } catch (Exception e) {
            // Manejo de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
        }
    }

}

