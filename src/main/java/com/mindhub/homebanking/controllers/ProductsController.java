package com.mindhub.homebanking.controllers;

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
import java.time.LocalDateTime;
import java.util.List;

import java.util.Arrays;

import static com.mindhub.homebanking.utils.CardUtils.getRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ProductsController {

    @GetMapping("/Products")
    public List<Products> getProducts() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.mercadolibre.com/sites/MLA/categories";
        ResponseEntity<Products[]> responseEntity = restTemplate.getForEntity(url, Products[].class);
        Products[] ProductsToList = responseEntity.getBody();
        List<Products> ProductsList = Arrays.asList(ProductsToList);
        return ProductsList;
    }

}