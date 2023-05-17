package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //Servicio para la transferencia de dinero entre cuentas propias y a terceros
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransactions(Authentication authentication,
                                                     @RequestParam String fromAccountNumber,
                                                     @RequestParam String toAccountNumber,
                                                     @RequestParam Double amount,
                                                     @RequestParam String description){


        Client clientConnect = clientRepository.findByEmail(authentication.getName());
        if(clientConnect == null) return new ResponseEntity<>("Client isn't authorization", HttpStatus.FORBIDDEN);

        if(amount == null || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        if(fromAccountNumber.equals(toAccountNumber)) return new ResponseEntity<>("Accounts must be not the same", HttpStatus.FORBIDDEN);

        Account accountOrigen = accountRepository.findByNumber(fromAccountNumber);
        Account accountDestino = accountRepository.findByNumber(toAccountNumber);

        if(accountOrigen == null) return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        if(accountDestino == null) return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);

        if(accountOrigen.getClient() != clientConnect) return new ResponseEntity<>("Change the account", HttpStatus.FORBIDDEN);

        if(accountOrigen.getBalance() < amount) return new ResponseEntity<>("Your Balance isnt enougth", HttpStatus.FORBIDDEN);

        Transaction transactionOrigen = new Transaction(TransactionType.DEBITO, amount*(-1), description+" - Cuenta: "+fromAccountNumber, LocalDateTime.now());
        Transaction transactionDestino = new Transaction(TransactionType.CREDITO, amount, description+" - Cuenta: "+toAccountNumber, LocalDateTime.now());

        accountOrigen.addTransaction(transactionOrigen);
        accountDestino.addTransaction(transactionDestino);

        transactionRepository.save(transactionOrigen);
        transactionRepository.save(transactionDestino);

        accountOrigen.setBalance(accountOrigen.getBalance()-amount);
        accountDestino.setBalance(accountDestino.getBalance()+amount);

        accountRepository.save(accountOrigen);
        accountRepository.save(accountDestino);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
