package com.mindhub.homebanking.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Locale;
import java.util.stream.Collectors;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Products;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static com.mindhub.homebanking.utils.CardUtils.getRandomNumber;
import static java.util.stream.Collectors.toList;
import java.time.LocalDateTime;
import java.util.List;
import java.net.URL;
import java.util.Arrays;
@RestController
@RequestMapping("/api")
public class messagesController {

    private final MessageSource messageSource;

    public messagesController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

//    @GetMapping("/greeting")
//    public String greeting(Model model, Locale locale) {
//        String greeting = messageSource.getMessage("greeting", null, locale);
//        model.addAttribute("greeting", greeting);
//        return "greeting";
//    }
    @GetMapping("/greeting")
    public String getGreeting() {
        String greeting = messageSource.getMessage("message.greeting", null, LocaleContextHolder.getLocale());
        return greeting;
    }

    @GetMapping("/list")
    public String getListTitle() {
        String listTitle = messageSource.getMessage("message.listTitle", null, LocaleContextHolder.getLocale());
        return listTitle;
    }

    @GetMapping("/products")
    public ResponseEntity<String> getTranslatedProducts() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.mercadolibre.com/sites/MLA/categories";
        ResponseEntity<Products[]> responseEntity = restTemplate.getForEntity(url, Products[].class);
        Products[] products = responseEntity.getBody();

        List<String> translatedProducts = Arrays.stream(products)
                .map(product -> messageSource.getMessage("message." + product.getId(), null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());

        // Convertir la lista de productos traducidos a formato JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(translatedProducts);

        return ResponseEntity.ok(jsonResponse);
    }
}