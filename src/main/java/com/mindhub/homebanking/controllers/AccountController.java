package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    //Obtener cuentas
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Obtener cliente por ID
    @GetMapping("accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    //Servicio para crear nueva cuenta
    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsClients(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> registerAccount(Authentication authentication) {

        Client clientConnect = clientRepository.findByEmail(authentication.getName());

        if(clientConnect != null){
            if(clientConnect.getAccounts().size() > 2){
                return new ResponseEntity<>("You have already 3 accounts", HttpStatus.FORBIDDEN);
            }else{
                Account account = new Account("VIN-"+getRandomNumber(10000001,100000000), LocalDateTime.now(),0.00);

                clientConnect.addAccount(account);

                accountRepository.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }else{
            return new ResponseEntity<>("Usuario no autenticado", HttpStatus.FORBIDDEN);
        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }




}
