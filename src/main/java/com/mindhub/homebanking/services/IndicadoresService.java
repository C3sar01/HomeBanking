package com.mindhub.homebanking.services;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IndicadoresService {

    public Map<String, Object> getIndicadores() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://mindicador.cl/api";
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> indicadores = responseEntity.getBody();
            indicadores.remove("version");
            indicadores.remove("fecha");
            indicadores.remove("autor");


        return indicadores;
    }
}
