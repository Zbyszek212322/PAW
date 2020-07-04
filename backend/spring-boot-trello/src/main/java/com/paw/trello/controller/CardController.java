package com.paw.trello.controller;

import com.paw.trello.dto.CardAddPost;
import com.paw.trello.dto.CardDto;
import com.paw.trello.dto.CardUpdatePost;
import com.paw.trello.entity.Card;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/all")
    public Iterable<CardDto> getAll() {
        return cardService.findAll();
    }

    @GetMapping("/get/{id}")
    public CardDto getById(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        return cardService.findById(id);
    }

    @GetMapping("/get/cardList/{id}")
    public List<CardDto> getByIdd(@PathVariable @RequestBody Long id) {
        return cardService.findAllCardsFromList(id);
    }

    @GetMapping("/get/table/{id}")
    public List<CardDto> getByTable(@PathVariable @RequestBody Long id) {
        return cardService.findAllCardsByTable(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCard(@RequestBody CardAddPost cardPost) throws TableNotFoundException {
        cardService.add(cardPost);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCard(@RequestBody CardUpdatePost cardUpdatePost) throws TableNotFoundException {
        cardService.update(cardUpdatePost);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCard(@RequestParam(name = "cardId") Long cardId) {
        cardService.deleteById(cardId);
        return new ResponseEntity<>("Card with ID:" + cardId + " deleted successfully", HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Card> updateCard(@RequestBody Card card) {
        Card cardd = cardService.save(card);
        return  new ResponseEntity<>(cardd, HttpStatus.OK);
    }

    @PutMapping("/moveRight/{id}")
    public ResponseEntity<String> moveRight(@PathVariable @RequestBody Long id) {
        cardService.moveRight(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/moveLeft/{id}")
    public ResponseEntity<String> moveLeft(@PathVariable @RequestBody Long id) {
        cardService.moveLeft(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
