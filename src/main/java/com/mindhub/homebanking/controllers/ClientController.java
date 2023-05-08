package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    //Obtener clientes
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
            return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    // Obtener cliente por ID
    @GetMapping("clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        return optionalClient.map(client -> ResponseEntity.ok(mapClientToDTO(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ClientDTO mapClientToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        return dto;
    }



    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Client> crearClient(@Validated @RequestBody Client client) {
        Client nuevoClient = clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoClient);
    }






}
