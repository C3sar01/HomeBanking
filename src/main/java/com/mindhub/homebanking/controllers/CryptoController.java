package com.mindhub.homebanking.controllers;
import java.util.List;
import java.util.Arrays;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mindhub.homebanking.models.Crypto;

@RestController
@RequestMapping("/api")
public class CryptoController {

    @GetMapping("/cryptos")
    public List<Crypto> getCryptos() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&sparkline=false&locale=es";
        ResponseEntity<Crypto[]> responseEntity = restTemplate.getForEntity(url, Crypto[].class);
        Crypto[] cryptoArray = responseEntity.getBody();
        List<Crypto> cryptos = Arrays.asList(cryptoArray);
        return cryptos;
    }
}