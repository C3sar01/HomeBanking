package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Indicadores;
import com.mindhub.homebanking.services.IndicadoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class IndicadoresController {

    private final IndicadoresService indicadoresService;

    @Autowired
    public IndicadoresController(IndicadoresService indicadoresService) {
        this.indicadoresService = indicadoresService;
    }

    @GetMapping("/indicadores")
    public Map<String, Object> getIndicadores() {
        return indicadoresService.getIndicadores();
    }
}

