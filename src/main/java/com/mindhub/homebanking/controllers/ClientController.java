package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

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


    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Client> crearClient(@Validated @RequestBody Client client) {
        Client nuevoClient = clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoClient);
    }






}
