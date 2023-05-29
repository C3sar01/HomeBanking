package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    //Servicio para la transferencia de dinero entre cuentas propias y a terceros
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransactions(Authentication authentication,
                                                     @RequestParam String fromAccountNumber,
                                                     @RequestParam String toAccountNumber,
                                                     @RequestParam Double amount,
                                                     @RequestParam String description) {


            Client clientConnect = clientRepository.findByEmail(authentication.getName());
            if (clientConnect == null) return new ResponseEntity<>("Client isn't authorization", HttpStatus.FORBIDDEN);

            if (amount == null || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty())
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

            if (fromAccountNumber.equals(toAccountNumber))
                return new ResponseEntity<>("Accounts must be not the same", HttpStatus.FORBIDDEN);

            Account accountOrigen = accountRepository.findByNumber(fromAccountNumber);
            Account accountDestino = accountRepository.findByNumber(toAccountNumber);

            if (accountOrigen == null) return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
            if (accountDestino == null) return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);

            if (accountOrigen.getClient() != clientConnect)
                return new ResponseEntity<>("Change the account", HttpStatus.FORBIDDEN);

            if (accountOrigen.getBalance() < amount)
                return new ResponseEntity<>("Your Balance isnt enougth", HttpStatus.FORBIDDEN);

            int points = (int) (amount / 1000);
            clientConnect.setPoints(clientConnect.getPoints() + points);
            clientRepository.save(clientConnect);
            System.out.println("Transacción exitosa. Puntos acumulados: " + clientConnect.getPoints());

            Transaction transactionOrigen = new Transaction(TransactionType.DEBITO, amount * (-1), description + " - Cuenta: " + fromAccountNumber, LocalDateTime.now());
            Transaction transactionDestino = new Transaction(TransactionType.CREDITO, amount, description + " - Cuenta: " + toAccountNumber, LocalDateTime.now());

            accountOrigen.addTransaction(transactionOrigen);
            accountDestino.addTransaction(transactionDestino);

            transactionRepository.save(transactionOrigen);
            transactionRepository.save(transactionDestino);

            accountOrigen.setBalance(accountOrigen.getBalance() - amount);
            accountDestino.setBalance(accountDestino.getBalance() + amount);

            accountRepository.save(accountOrigen);
            accountRepository.save(accountDestino);

            //return new ResponseEntity<>(HttpStatus.CREATED);
            //Account originAccount = this.accountRepository.findByNumber(fromAccountNumber);

        return ResponseEntity.ok("Transacción exitosa. Puntos acumulados: " + clientConnect.getPoints());

    }

    //Exportar cartola en PDF
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePDF(@RequestParam(required = true) String accountNumber) {
        try {
            Account account = accountRepository.findByNumber(accountNumber);

            if (account == null) {
                return ResponseEntity.notFound().build();
            }

            List<TransactionDTO> transactionDTOList = account.getTransactions().stream()
                    .map(TransactionDTO::new)
                    .collect(Collectors.toList());

            PDFGenerator exporter = new PDFGenerator(transactionDTOList);
            byte[] pdfBytes = exporter.export();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "transactions.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    @Transactional
//    @PostMapping("/pointsTransaction")
//    public String pointsTransaction(@RequestParam double amount, @RequestParam String accountFromNumber,Authentication authentication) {
//
//        Client client = clientRepository.findByEmail(authentication.getName());
//        Account originAccount = this.accountRepository.findByNumber(accountFromNumber);
//
//        if (client != null) {
//            int points = (int) (amount / 1000);
//
//            Transaction transaction = new Transaction();
//            transaction.setType(TransactionType.CREDITO);
//            transaction.setAmount(amount);
//            transaction.setDescription("Transacción realizada");
//            transaction.setDate(LocalDateTime.now());
//            transaction.setAccount(originAccount);
//            transactionRepository.save(transaction);
//
//            client.setPoints(client.getPoints() + points);
//            originAccount.addTransactions(transaction);
//            clientRepository.save(client);
//
//
//            return "Transacción exitosa. Puntos acumulados: " + client.getPoints();
//        }
//        return "Usuario no encontrado";
//    }

    @GetMapping("/pointsTransaction/current")
    public ResponseEntity<ClientDTO> getPointsTransaction(Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            ClientDTO clientDTO = new ClientDTO(client);
            return ResponseEntity.ok(clientDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

