package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.CardUtils.getRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    //Obtener clientes
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
            return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    // Obtener cliente por ID
    @GetMapping("clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }


    //Nuevo cliente
    @GetMapping("clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        Account account = new Account("VIN-" + getRandomNumber(10000001, 100000000), LocalDateTime.now(), 0.00);

        client.addAccount(account);

        accountRepository.save(account);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/web/clients/{id}")
    public void setStatus(@PathVariable Long id){
        Client client = clientRepository.findById(id).get();
        client.setStatus(!client.isStatus());
        clientRepository.save(client);
    }



}
