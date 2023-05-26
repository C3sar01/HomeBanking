package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.MailService;
import com.mindhub.homebanking.services.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mail")
public class EmailController {
    @Autowired
    private MailService mailService;

    @Autowired
    public EmailController(MailService mailService) {
        this.mailService = mailService;
    }
    @Autowired
    private AccountRepository accountRepository;
    @GetMapping("/sendPdf")
    public void generatePDF(HttpServletResponse response,
                            @RequestParam(required = true) String accountNumber)
            throws DocumentException, IOException {

        Account account = accountRepository.findByNumber(accountNumber);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Generar el PDF y almacenarlo en el ByteArrayOutputStream
        PDFGeneratorService pdfGenerator = new PDFGeneratorService(account.getTransactions().stream()
                .map(TransactionDTO::new).collect(Collectors.toList()));

        pdfGenerator.export(response);

        // Configurar la respuesta HTTP
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Enviar el PDF al servicio de JavaMailer
        byte[] pdfBytes = outputStream.toByteArray();
        mailService.sendEmailWithAttachment(pdfBytes);

        // Cerrar el flujo de salida
        outputStream.close();
    }
    private String savePDFInServer(byte[] pdfData) throws IOException {
        String filePath = "F:\\"; // Especifica la ruta donde deseas guardar el archivo en el servidor

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfData);
        } catch (IOException e) {
            // Manejar el error de escritura del archivo según tus necesidades
            e.printStackTrace();
        }

        return filePath;
    }


}