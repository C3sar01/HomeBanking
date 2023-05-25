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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatGPTController {

    private ChatGPTService chatGPTService;

    @Autowired
    private AccountRepository accountRepository;

    public ChatGPTController(ChatGPTService chatGPTService, AccountRepository accountRepository) {
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
        //Pasa json al promt y posteriormente genera la respuesta
        String prompt = generatePrompt(jsonTransactions);
        ChatGPTResponse chatGPTResponse = chatGPTService.getChatCPTResponse(prompt);

        return new ResponseEntity<>(chatGPTResponse, HttpStatus.OK);
        }catch (JsonProcessingException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Método que recibe el json con transacciones y genera el Promt.
    private String generatePrompt(String jsonTransactions ) {
        String prompt = "Puntaje del cliente: " + jsonTransactions;
        prompt += "Comportate como asesor financiero y realiza un calculo en base a las transacciones obtenidas de tipo credito y " +
                "debito, asignando un puntaje al cliente en una escala del 1 al 10..." +
                "Si posee más transacciones debito, su puntaje será menor, por ende deberás " +
                "entregar de manera breve recomendaciones sobre como ahorrar dinero," +
                "en cambio, si el cliente posee mayor cantidad de transferencias con credito, se le asignará un puntaje más alto y " +
                "le debes dar recomendaciones" +
                "sobre como invertir su dinero, además felicitalo. Un puntaje mayor a 5 implica que tiene más credito, osea un puntaje alto." +
                "Un puntaje menor a 5 implica mayor cantidad de debito, osea un puntaje bajo. Además, si posee un puntaje bajo da a conocer " +
                "las consecuencias" +
                "de un mal comportamiento financiero";
        return prompt;
    }

//    private double calculateCustomerScore(String accountNumber) {
//        Account account = accountRepository.findByNumber(accountNumber);
//        List<TransactionDTO> transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
//
//        int debitCount = 0;
//        int creditCount = 0;
//
//        for (TransactionDTO transaction : transactions) {
//            if (transaction.getType() == TransactionType.DEBITO) {
//                debitCount++;
//            } else if (transaction.getType() == TransactionType.CREDITO) {
//                creditCount++;
//            }
//        }
//
//        return calculateScoreFromCounts(debitCount, creditCount);
//    }
//
//    private double calculateScoreFromCounts(int debitCount, int creditCount) {
//        int totalTransactions = debitCount + creditCount;
//        if (totalTransactions == 0) {
//            return 5.0; // Puntaje neutral si no hay transacciones
//        }
//
//        double debitRatio = (double) debitCount / totalTransactions; //Es la proporción de transacciones de débito con respecto al total de transacciones. Se calcula dividiendo el número de transacciones de débito (debitCount) entre el total de transacciones (totalTransactions).
//        double creditRatio = (double) creditCount / totalTransactions; //Es la proporción de transacciones de crédito con respecto al total de transacciones. Se calcula dividiendo el número de transacciones de crédito (creditCount) entre el total de transacciones (totalTransactions).
//
//        double customerScore = 1 + 9 * creditRatio;// Escala del 1 al 10
//        System.out.println(customerScore);
//        return customerScore;
//    }



}
