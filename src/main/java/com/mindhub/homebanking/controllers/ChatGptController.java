package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ChatBotInputRequest;
import com.mindhub.homebanking.models.ChatGPTResponse;
import com.mindhub.homebanking.models.TransactionType;
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
    public ResponseEntity<ChatGPTResponse> processInputRequest(@RequestBody ChatBotInputRequest chatbotInputRequest) {
        String accountNumber = chatbotInputRequest.getAccountNumber();
        double customerScore = calculateCustomerScore(accountNumber);
        String prompt = generatePrompt(customerScore);

        ChatGPTResponse chatGPTResponse = chatGPTService.getChatCPTResponse(prompt);
        return new ResponseEntity<>(chatGPTResponse, HttpStatus.OK);
    }

    private double calculateCustomerScore(String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        List<TransactionDTO> transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());

        int debitCount = 0;
        int creditCount = 0;

        for (TransactionDTO transaction : transactions) {
            if (transaction.getType() == TransactionType.DEBITO) {
                debitCount++;
            } else if (transaction.getType() == TransactionType.CREDITO) {
                creditCount++;
            }
        }

        return calculateScoreFromCounts(debitCount, creditCount);
    }

    private double calculateScoreFromCounts(int debitCount, int creditCount) {
        int totalTransactions = debitCount + creditCount;
        if (totalTransactions == 0) {
            return 5.0; // Puntaje neutral si no hay transacciones
        }

        double debitRatio = (double) debitCount / totalTransactions; //Es la proporción de transacciones de débito con respecto al total de transacciones. Se calcula dividiendo el número de transacciones de débito (debitCount) entre el total de transacciones (totalTransactions).
        double creditRatio = (double) creditCount / totalTransactions; //Es la proporción de transacciones de crédito con respecto al total de transacciones. Se calcula dividiendo el número de transacciones de crédito (creditCount) entre el total de transacciones (totalTransactions).

        double customerScore = 1 + 9 * creditRatio;// Escala del 1 al 10
        System.out.println(customerScore);
        return customerScore;
    }


    private String generatePrompt(double customerScore) {
        String prompt = "Customer Score: " + customerScore + "\n";
        prompt += "Por favor entrega recomendaciones acerca de:  ";
        if (customerScore >= 5.0) {
            prompt += "Comportate como un asesor financiero, de manera muy breve felicítame y dame formas de invertir mi dinero ya que mi puntaje es alto, además, muéstrame mi puntaje como cliente indicando que este se calcula en una escala del 1 al 10.";
        } else {
            prompt += "Comportate como un asesor financiero, de manera muy breve indícame las consecuencias de no tener un buen comportamiento financier y  dame formas de ahorrar, además, muéstrame mi puntaje como cliente indicando que este se calcula en una escala del 1 al 10";
        }
        return prompt;
    }
}
