package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ResponseDTO;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor //Inyectar dependencias
public class ChatGptController {

    private final ChatgptService chatgptService; //Servicio de la dependencia

    @PostMapping("/send")
    public ResponseDTO<String> send(@RequestBody String message){
        log.info("message is: {}", message);
        String responseMessage = chatgptService.sendMessage(message);
        log.info("response is:", responseMessage);


        return ResponseDTO.success(responseMessage);
    }
}
