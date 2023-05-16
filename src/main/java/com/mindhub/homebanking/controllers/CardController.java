package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    public ClientRepository clientRepository;
    @Autowired
    public CardRepository cardRepository;

    @PostMapping("clients/current/cards")
    public ResponseEntity<Object> registerCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {


        Client clientConnect = clientRepository.findByEmail(authentication.getName());

        AtomicInteger count= new AtomicInteger();

        if (clientConnect != null) {

            Stream<Card> stream = clientConnect.getCards().stream();
            stream.forEach((card) -> {if (card.getType() == cardType) count.getAndIncrement();});

            if (count.get() > 2) {
                return new ResponseEntity<>("You have already 3 cards of that type", HttpStatus.FORBIDDEN);
            } else {

                Card card = new Card( clientConnect.getFirstName()+" "+clientConnect.getLastName(),
                        cardType, cardColor, CardUtils.getRandomNumber(1001,10000),CardUtils.
                        getRandomNumberCvv(101,1000),
                        LocalDate.now(),
                        LocalDate.now().plusYears(5));

                clientConnect.addCard(card);

                cardRepository.save(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("Usuario no autenticado", HttpStatus.FORBIDDEN);
        }
    }


}
