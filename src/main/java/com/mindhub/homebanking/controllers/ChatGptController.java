package com.mindhub.homebanking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ChatBotInputRequest;
import com.mindhub.homebanking.models.ChatGPTResponse;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatGptController {

    private ChatGPTService chatGPTService;

    @Autowired
    private AccountRepository accountRepository;

    public ChatGptController(ChatGPTService chatGPTService, AccountRepository accountRepository) {
        this.chatGPTService = chatGPTService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatGPTResponse> processInputRequest(@RequestBody ChatBotInputRequest chatbotInputRequest) throws JsonProcessingException {
        try {
        //Entrega lista de clientes a chatgpt
        String accountNumber = chatbotInputRequest.getAccountNumber();
        Account account = accountRepository.findByNumber(accountNumber);
        List<TransactionDTO> transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        //Transforma lista a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        //Le pasa transactions para convertir
        String jsonTransactions = objectMapper.writeValueAsString(transactions);
        //Pasa json al prompt y posteriormente genera la respuesta
        String prompt = generatePrompt(jsonTransactions);
        ChatGPTResponse chatGPTResponse = chatGPTService.getChatCPTResponse(prompt);

        return new ResponseEntity<>(chatGPTResponse, HttpStatus.OK);
        }catch (JsonProcessingException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Método que recibe el json con transacciones y genera el Promt.
    private String generatePrompt(String jsonTransactions ) {
        String prompt = "Puntaje obtenido" + jsonTransactions;
        prompt += "Actúa como un Asesor financiero. Realiza la siguiente tarea: Evaluar la lista de transacciones de tipo debito y credito, asignando un puntaje " +
                "en una escala del 1 al 10 en base a una proporción de transacciones totales." +
                "Si tengo más transacciones de debito, me debes asignar " +
                "menor puntaje y darme recomendaciones de ahorro y las consecuencias de un mal manejo financiero. Pero si tengo mayor cantidad de " +
                "transacciones de credito, " +
                "se me debe asignar un mayor puntaje y darme " +
                "una lista de 5 recomendaciones de como invertir mi dinero y felicitarme. " +
                "La respuesta está dirigida a: A un cliente. " +
                "Recuerda ser breve y no extenderte demasiado con el texto." +
                "Recuerda dar recomendaciones de ahorro solo en el caso de que el puntaje sea menor a 5." +
                "Si el puntaje es mayor a 5, solamente dar recomendaciones de inversión";
        return prompt;
    }

}
